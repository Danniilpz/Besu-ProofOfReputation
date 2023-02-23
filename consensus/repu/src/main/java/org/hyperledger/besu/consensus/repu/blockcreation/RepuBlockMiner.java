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
import org.hyperledger.besu.consensus.repu.contracts.TestContract;
import org.hyperledger.besu.crypto.NodeKey;
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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.function.Function;

public class RepuBlockMiner extends BlockMiner<RepuBlockCreator> {

  private static final String HTTP_URL = "http://localhost:8545";
  private static final String CONTRACT_ADDRESS = "0x4D2D24899c0B115a1fce8637FCa610Fe02f1909e";
  private static final BigInteger GAS_PRICE = new BigInteger("500000");
  private static final BigInteger GAS_LIMIT = new BigInteger("3000000");
  private static final Logger LOG = LoggerFactory.getLogger(BlockMiner.class);
  private final HttpService httpService;
  private final Web3j web3j;
  private final Address localAddress;
  private final NodeKey nodeKey;
  private TestContract testContract;

  public RepuBlockMiner(
      final Function<BlockHeader, RepuBlockCreator> blockCreator,
      final ProtocolSchedule protocolSchedule,
      final ProtocolContext protocolContext,
      final Subscribers<MinedBlockObserver> observers,
      final AbstractBlockScheduler scheduler,
      final BlockHeader parentHeader,
      final Address localAddress) {
    super(blockCreator, protocolSchedule, protocolContext, observers, scheduler, parentHeader);
    this.nodeKey = blockCreator.apply(parentHeader).getNodeKey();
    this.localAddress = localAddress;
    this.httpService = new HttpService(HTTP_URL);
    this.web3j = Web3j.build(httpService);
    testContract = new TestContract(CONTRACT_ADDRESS, web3j,
            getCredentials(), new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
  }

  @Override
  protected boolean mineBlock() throws Exception {
    if (RepuHelpers.addressIsAllowedToProduceNextBlock(
        localAddress, protocolContext, parentHeader)) {
      if(parentHeader.getNumber() == 0)
        testContract = TestContract.deploy(web3j, getCredentials(), new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
      //LOG.info("Count: "+testContractGetCount()+" Number: "+testContractGetNumber());
      boolean result = super.mineBlock();
      //testContractSetCount(5);
      return result;
    }

    return true; // terminate mining.
  }

  public Credentials getCredentials(){
    return Credentials.create(nodeKey.getPrivateKey().getKey(),nodeKey.getPublicKey().toString());
  }
  public String testContractGetCount() throws Exception { return testContract.getCount().send().toString(); }

  public String testContractGetNumber() throws Exception { return testContract.getNumber().send().toString(); }

  public void testContractIncrementCount() throws Exception { testContract.incrementCount().send(); }

  public void testContractSetCount(int count) throws Exception { testContract.setCount(new BigInteger(String.valueOf(count))).send(); }

}
