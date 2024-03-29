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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.consensus.common.ConsensusHelpers;
import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.repu.RepuExtraData;
import org.hyperledger.besu.consensus.repu.RepuHelpers;
import org.hyperledger.besu.consensus.repu.contracts.RepuContract;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.blockcreation.AbstractBlockScheduler;
import org.hyperledger.besu.ethereum.blockcreation.AbstractMinerExecutor;
import org.hyperledger.besu.ethereum.chain.MinedBlockObserver;
import org.hyperledger.besu.ethereum.chain.PoWObserver;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.MiningParameters;
import org.hyperledger.besu.ethereum.core.Util;
import org.hyperledger.besu.ethereum.eth.transactions.sorter.AbstractPendingTransactionsSorter;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.util.Subscribers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class RepuMinerExecutor extends AbstractMinerExecutor<RepuBlockMiner> {

    private final Address localAddress;
    private final NodeKey nodeKey;
    private final String port;
    private final EpochManager epochManager;

    public RepuMinerExecutor(
            final ProtocolContext protocolContext,
            final ProtocolSchedule protocolSchedule,
            final AbstractPendingTransactionsSorter pendingTransactions,
            final NodeKey nodeKey,
            final String port,
            final MiningParameters miningParams,
            final AbstractBlockScheduler blockScheduler,
            final EpochManager epochManager) {
        super(protocolContext, protocolSchedule, pendingTransactions, miningParams, blockScheduler);
        this.nodeKey = nodeKey;
        this.port = port;
        this.localAddress = Util.publicKeyToAddress(nodeKey.getPublicKey());
        this.epochManager = epochManager;
    }

    @Override
    public RepuBlockMiner createMiner(
            final Subscribers<MinedBlockObserver> observers,
            final Subscribers<PoWObserver> ethHashObservers,
            final BlockHeader parentHeader) {
        final Function<BlockHeader, RepuBlockCreator> blockCreator =
                (header) ->
                        new RepuBlockCreator(
                                localAddress, // TOOD(tmm): This can be removed (used for voting not coinbase).
                                () -> targetGasLimit.map(AtomicLong::longValue),
                                this::calculateExtraData,
                                pendingTransactions,
                                protocolContext,
                                protocolSchedule,
                                nodeKey,
                                port,
                                minTransactionGasPrice,
                                minBlockOccupancyRatio,
                                header,
                                epochManager);

        return new RepuBlockMiner(
                blockCreator,
                protocolSchedule,
                protocolContext,
                observers,
                blockScheduler,
                parentHeader,
                localAddress);
    }

    @Override
    public Optional<Address> getCoinbase() {
        return Optional.of(localAddress);
    }

    @VisibleForTesting
    Bytes calculateExtraData(final BlockHeader parentHeader) {
        final List<Address> validators = Lists.newArrayList();

        final Bytes vanityDataToInsert =
                ConsensusHelpers.zeroLeftPad(extraData, RepuExtraData.EXTRA_VANITY_LENGTH);
        // Building ON TOP of canonical head, if the next block is epoch, include validators.
        if (epochManager.isEpochBlock(parentHeader.getNumber() + 1)) {

            if (RepuHelpers.repuContract == null) {
                validators.add(Address.fromHexString(RepuContract.INITIAL_VALIDATOR));
            } else {
                validators.addAll(RepuHelpers.getValidators());
            }
        }

        return RepuExtraData.encodeUnsealed(vanityDataToInsert, validators);
    }
}
