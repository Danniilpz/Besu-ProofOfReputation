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
import org.hyperledger.besu.consensus.repu.contracts.ProxyContract;
import org.hyperledger.besu.consensus.repu.contracts.TestRepuContract;
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

  private static final BigInteger GAS_PRICE = new BigInteger("500000");
  private static final BigInteger GAS_LIMIT = new BigInteger("3000000");
  private final Web3j web3j;
  private final NodeKey nodeKey;
  private static ProxyContract proxyContract;
  private static TestRepuContract repuContract;
  private static boolean contractDeployed = false;
  private static boolean contractDeploying = false;
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
    this.nodeKey = blockCreator.apply(parentHeader).getNodeKey();
    String port = blockCreator.apply(parentHeader).getPort();
    String httpUrl = "http://localhost:" + port;
    this.web3j = Web3j.build(new HttpService(httpUrl));
    if (repuContract == null) {
      try {
        getRepuContract();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  protected boolean mineBlock() throws Exception {
    //LOG.warn("I am "+localAddress.toString()+" with port "+port);
    /*if ((!contractDeployed
            && RepuHelpers.addressIsAllowedToProduceNextBlock(localAddress, protocolContext, parentHeader)) ||
        (contractDeployed
            && localAddress.toString().equals(testContract.nextValidator()))) {*/
    if (RepuHelpers.addressIsAllowedToProduceNextBlock(localAddress, protocolContext, parentHeader)) {

      boolean mined = super.mineBlock();

      if (!contractDeployed && !contractDeploying)
        deployContracts();

      if(repuContract != null) {
        LOG.info("Next validator: "+ repuContract.nextValidator());
        LOG.info("Validators: "+ repuContract.getValidators().toString());
        repuContract.updateValidator();
      }
      //if(proxyContract != null) LOG.info("Consensus address: "+ proxyContract.getConsensusAddress());
      return mined;
    }

    return true; // terminate mining.
  }

  public void getRepuContract() throws Exception {

    if(!contractDeploying && parentHeader.getNumber() > 1){
      contractDeployed = true;
      proxyContract = new ProxyContract(web3j, getCredentials(), new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
      repuContract =
          new TestRepuContract(proxyContract.getConsensusAddress(), web3j, getCredentials(), new StaticGasProvider(GAS_PRICE, GAS_LIMIT));

      RepuHelpers.repuContract = repuContract;

      LOG.info("Detected consensus contract in address {}", proxyContract.getConsensusAddress());
    }
    else{
      repuContract = null;
      contractDeployed = false;
    }
  }

  public void deployContracts() throws Exception {
    contractDeploying = true;

    proxyContract = ProxyContract.deploy(web3j, getCredentials(),
            new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();

    repuContract = TestRepuContract.deploy(web3j, getCredentials(),
            new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();

    RepuHelpers.repuContract = repuContract;

    LOG.info("Deployed proxy contract into {} and consensus contract into {}",
            proxyContract.getContractAddress(), repuContract.getContractAddress());

    contractDeployed = true;
    contractDeploying =false;
  }

  public Credentials getCredentials(){
    return Credentials.create(nodeKey.getPrivateKey().getKey(),nodeKey.getPublicKey().toString());
  }

}
