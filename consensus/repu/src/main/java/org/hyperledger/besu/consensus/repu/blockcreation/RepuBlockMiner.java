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

import org.hyperledger.besu.consensus.repu.RepuHelpers;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.blockcreation.AbstractBlockScheduler;
import org.hyperledger.besu.ethereum.blockcreation.BlockMiner;
import org.hyperledger.besu.ethereum.chain.MinedBlockObserver;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.util.Subscribers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import java.util.function.Function;

public class RepuBlockMiner extends BlockMiner<RepuBlockCreator> {

    private final Address localAddress;
    private static final Logger LOG = LoggerFactory.getLogger(BlockMiner.class);

    public RepuBlockMiner(
            final Function<BlockHeader, RepuBlockCreator> blockCreator,
            final ProtocolSchedule protocolSchedule,
            final ProtocolContext protocolContext,
            final Subscribers<MinedBlockObserver> observers,
            final AbstractBlockScheduler scheduler,
            final BlockHeader parentHeader,
            final Address localAddress) {
        super(blockCreator, protocolSchedule, protocolContext, observers, scheduler, parentHeader);
        this.localAddress = localAddress;
        RepuHelpers.setNodeKey(blockCreator.apply(parentHeader).getNodeKey());
        String port = blockCreator.apply(parentHeader).getPort();
        String httpUrl = "http://localhost:" + port;
        RepuHelpers.setWeb3j(Web3j.build(new HttpService(httpUrl)));
        RepuHelpers.getRepuContract(parentHeader);
    }

    @Override
    protected boolean mineBlock() throws Exception {
        //LOG.info("trying to mine");
        if (RepuHelpers.addressIsAllowedToProduceNextBlock(
                localAddress, protocolContext, parentHeader)) {

            boolean mined = super.mineBlock();

            RepuHelpers.checkContractsAreDeployed();

            RepuHelpers.updateValidator();

            return mined;
        }

        return true; // terminate mining.
    }


}
