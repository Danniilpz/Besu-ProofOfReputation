/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.besu.controller;

import org.hyperledger.besu.config.RepuConfigOptions;
import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.consensus.repu.RepuContext;
import org.hyperledger.besu.consensus.repu.RepuMiningTracker;
import org.hyperledger.besu.consensus.repu.RepuProtocolSchedule;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuBlockScheduler;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuMinerExecutor;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuMiningCoordinator;
import org.hyperledger.besu.consensus.repu.jsonrpc.RepuJsonRpcMethods;
import org.hyperledger.besu.consensus.common.BlockInterface;
import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.common.validator.blockbased.BlockValidatorProvider;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.api.jsonrpc.methods.JsonRpcMethods;
import org.hyperledger.besu.ethereum.blockcreation.MiningCoordinator;
import org.hyperledger.besu.ethereum.chain.Blockchain;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.MiningParameters;
import org.hyperledger.besu.ethereum.core.Util;
import org.hyperledger.besu.ethereum.eth.manager.EthProtocolManager;
import org.hyperledger.besu.ethereum.eth.sync.state.SyncState;
import org.hyperledger.besu.ethereum.eth.transactions.TransactionPool;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.ethereum.worldstate.WorldStateArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RepuBesuControllerBuilder extends BesuControllerBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(RepuBesuControllerBuilder.class);

  private Address localAddress;
  private EpochManager epochManager;
  private long secondsBetweenBlocks;
  private final BlockInterface blockInterface = new RepuBlockInterface();

  @Override
  protected void prepForBuild() {
    localAddress = Util.publicKeyToAddress(nodeKey.getPublicKey());
    final RepuConfigOptions repuConfig = configOptionsSupplier.get().getRepuConfigOptions();
    final long blocksPerEpoch = repuConfig.getEpochLength();
    secondsBetweenBlocks = repuConfig.getBlockPeriodSeconds();

    epochManager = new EpochManager(blocksPerEpoch);
  }

  @Override
  protected JsonRpcMethods createAdditionalJsonRpcMethodFactory(
      final ProtocolContext protocolContext) {
    return new RepuJsonRpcMethods(protocolContext);
  }

  @Override
  protected MiningCoordinator createMiningCoordinator(
      final ProtocolSchedule protocolSchedule,
      final ProtocolContext protocolContext,
      final TransactionPool transactionPool,
      final MiningParameters miningParameters,
      final SyncState syncState,
      final EthProtocolManager ethProtocolManager) {
    final RepuMinerExecutor miningExecutor =
        new RepuMinerExecutor(
            protocolContext,
            protocolSchedule,
            transactionPool.getPendingTransactions(),
            nodeKey,
            port,
            miningParameters,
            new RepuBlockScheduler(
                clock,
                localAddress,
                secondsBetweenBlocks),
            epochManager);
    final RepuMiningCoordinator miningCoordinator =
        new RepuMiningCoordinator(
            protocolContext.getBlockchain(),
            miningExecutor,
            syncState,
            new RepuMiningTracker(localAddress));
    miningCoordinator.addMinedBlockObserver(ethProtocolManager);

    // Repu mining is implicitly enabled.
    miningCoordinator.enable();
    return miningCoordinator;
  }

  @Override
  protected ProtocolSchedule createProtocolSchedule() {
    return RepuProtocolSchedule.create(
        configOptionsSupplier.get(),
        nodeKey,
        privacyParameters,
        isRevertReasonEnabled,
        evmConfiguration);
  }

  @Override
  protected void validateContext(final ProtocolContext context) {
    final BlockHeader genesisBlockHeader = context.getBlockchain().getGenesisBlock().getHeader();

    if (blockInterface.validatorsInBlock(genesisBlockHeader).isEmpty()) {
      LOG.warn("Genesis block contains no signers - chain will not progress.");
    }
  }

  @Override
  protected PluginServiceFactory createAdditionalPluginServices(
      final Blockchain blockchain, final ProtocolContext protocolContext) {
    return new RepuQueryPluginServiceFactory(blockchain, nodeKey);
  }

  @Override
  protected RepuContext createConsensusContext(
      final Blockchain blockchain,
      final WorldStateArchive worldStateArchive,
      final ProtocolSchedule protocolSchedule) {
    return new RepuContext(
        BlockValidatorProvider.nonForkingValidatorProvider(
            blockchain, epochManager, blockInterface),
        epochManager,
        blockInterface);
  }

  @Override
  public MiningParameters getMiningParameterOverrides(final MiningParameters fromCli) {
    // Repu mines by default, reflect that with in the mining parameters:
    return new MiningParameters.Builder(fromCli).miningEnabled(true).build();
  }
}
