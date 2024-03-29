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
package org.hyperledger.besu.consensus.repu.blockcreation;

import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.common.validator.ValidatorVote;
import org.hyperledger.besu.consensus.repu.RepuBlockHashing;
import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.consensus.repu.RepuContext;
import org.hyperledger.besu.consensus.repu.RepuExtraData;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.blockcreation.AbstractBlockCreator;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderFunctions;
import org.hyperledger.besu.ethereum.core.SealableBlockHeader;
import org.hyperledger.besu.ethereum.core.Util;
import org.hyperledger.besu.ethereum.core.BlockHeaderBuilder;
import org.hyperledger.besu.ethereum.eth.transactions.sorter.AbstractPendingTransactionsSorter;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.ethereum.mainnet.ScheduleBasedBlockHeaderFunctions;

import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkState;

public class RepuBlockCreator extends AbstractBlockCreator {

  private final NodeKey nodeKey;
  private final EpochManager epochManager;
  private final String port;

  public RepuBlockCreator(
      final Address coinbase,
      final Supplier<Optional<Long>> targetGasLimitSupplier,
      final ExtraDataCalculator extraDataCalculator,
      final AbstractPendingTransactionsSorter pendingTransactions,
      final ProtocolContext protocolContext,
      final ProtocolSchedule protocolSchedule,
      final NodeKey nodeKey,
      final String port,
      final Wei minTransactionGasPrice,
      final Double minBlockOccupancyRatio,
      final BlockHeader parentHeader,
      final EpochManager epochManager) {
    super(
        coinbase,
        __ -> Util.publicKeyToAddress(nodeKey.getPublicKey()),
        targetGasLimitSupplier,
        extraDataCalculator,
        pendingTransactions,
        protocolContext,
        protocolSchedule,
        minTransactionGasPrice,
        minBlockOccupancyRatio,
        parentHeader);
    this.nodeKey = nodeKey;
    this.port = port;
    this.epochManager = epochManager;
  }

  public NodeKey getNodeKey() {
    return nodeKey;
  }

  public String getPort() {
    return port;
  }

  /**
   * Responsible for signing (hash of) the block (including MixHash and Nonce), and then injecting
   * the seal into the extraData. This is called after a suitable set of transactions have been
   * identified, and all resulting hashes have been inserted into the passed-in SealableBlockHeader.
   *
   * @param sealableBlockHeader A block header containing StateRoots, TransactionHashes etc.
   * @return The blockhead which is to be added to the block being proposed.
   */
  @Override
  protected BlockHeader createFinalBlockHeader(final SealableBlockHeader sealableBlockHeader) {
    final BlockHeaderFunctions blockHeaderFunctions =
        ScheduleBasedBlockHeaderFunctions.create(protocolSchedule);

    final BlockHeaderBuilder builder =
        BlockHeaderBuilder.create()
            .populateFrom(sealableBlockHeader)
            .mixHash(Hash.ZERO)
            .blockHeaderFunctions(blockHeaderFunctions);

    final Optional<ValidatorVote> vote = determineRepuVote(sealableBlockHeader);
    final BlockHeaderBuilder builderIncludingProposedVotes =
        RepuBlockInterface.createHeaderBuilderWithVoteHeaders(builder, vote);
    final RepuExtraData sealedExtraData =
        constructSignedExtraData(builderIncludingProposedVotes.buildBlockHeader());

    // Replace the extraData in the BlockHeaderBuilder, and return header.
    return builderIncludingProposedVotes.extraData(sealedExtraData.encode()).buildBlockHeader();
  }

  private Optional<ValidatorVote> determineRepuVote(
      final SealableBlockHeader sealableBlockHeader) {
    if (epochManager.isEpochBlock(sealableBlockHeader.getNumber())) {
      return Optional.empty();
    } else {
      final RepuContext repuContext = protocolContext.getConsensusContext(RepuContext.class);
      checkState(
          repuContext.getValidatorProvider().getVoteProviderAtHead().isPresent(),
          "Repu requires a vote provider");
      return repuContext
          .getValidatorProvider()
          .getVoteProviderAtHead()
          .get()
          .getVoteAfterBlock(parentHeader, Util.publicKeyToAddress(nodeKey.getPublicKey()));
    }
  }

  /**
   * Produces a RepuExtraData object with a populated proposerSeal. The signature in the block is
   * generated from the Hash of the header (minus proposer and committer seals) and the nodeKeys.
   *
   * @param headerToSign An almost fully populated header (proposer and committer seals are empty)
   * @return Extra data containing the same vanity data and validators as extraData, however
   *     proposerSeal will also be populated.
   */
  private RepuExtraData constructSignedExtraData(final BlockHeader headerToSign) {
    final RepuExtraData extraData = RepuExtraData.decode(headerToSign);
    final Hash hashToSign =
        RepuBlockHashing.calculateDataHashForProposerSeal(headerToSign, extraData);
    return new RepuExtraData(
        extraData.getVanityData(),
        nodeKey.sign(hashToSign),
        extraData.getValidators(),
        headerToSign);
  }
}
