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
package org.hyperledger.besu.consensus.repu.jsonrpc;

import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.consensus.common.validator.blockbased.BlockValidatorProvider;
import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.consensus.repu.RepuContext;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.Discard;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.Propose;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.RepuGetSignerMetrics;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.RepuGetSigners;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.RepuGetSignersAtHash;
import org.hyperledger.besu.consensus.repu.jsonrpc.methods.RepuProposals;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.api.jsonrpc.RpcApis;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.methods.JsonRpcMethod;
import org.hyperledger.besu.ethereum.api.jsonrpc.methods.ApiGroupJsonRpcMethods;
import org.hyperledger.besu.ethereum.api.query.BlockchainQueries;
import org.hyperledger.besu.ethereum.chain.MutableBlockchain;
import org.hyperledger.besu.ethereum.worldstate.WorldStateArchive;

import java.util.Map;

public class RepuJsonRpcMethods extends ApiGroupJsonRpcMethods {
  private final ProtocolContext context;

  public RepuJsonRpcMethods(final ProtocolContext context) {
    this.context = context;
  }

  @Override
  protected String getApiGroup() {
    return RpcApis.REPU.name();
  }

  @Override
  protected Map<String, JsonRpcMethod> create() {
    final MutableBlockchain blockchain = context.getBlockchain();
    final WorldStateArchive worldStateArchive = context.getWorldStateArchive();
    final BlockchainQueries blockchainQueries =
        new BlockchainQueries(blockchain, worldStateArchive);
    final ValidatorProvider validatorProvider =
        context.getConsensusContext(RepuContext.class).getValidatorProvider();

    // Must create our own voteTallyCache as using this would pollute the main voteTallyCache
    final ValidatorProvider readOnlyValidatorProvider =
        createValidatorProvider(context, blockchain);

    return mapOf(
        new RepuGetSigners(blockchainQueries, readOnlyValidatorProvider),
        new RepuGetSignersAtHash(blockchainQueries, readOnlyValidatorProvider),
        new Propose(validatorProvider),
        new Discard(validatorProvider),
        new RepuProposals(validatorProvider),
        new RepuGetSignerMetrics(
            readOnlyValidatorProvider, new RepuBlockInterface(), blockchainQueries));
  }

  private ValidatorProvider createValidatorProvider(
      final ProtocolContext context, final MutableBlockchain blockchain) {
    final EpochManager epochManager =
        context.getConsensusContext(RepuContext.class).getEpochManager();
    final RepuBlockInterface repuBlockInterface = new RepuBlockInterface();
    return BlockValidatorProvider.nonForkingValidatorProvider(
        blockchain, epochManager, repuBlockInterface);
  }
}
