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
import org.hyperledger.besu.consensus.repu.contracts.TestContractRepu;
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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RepuBlockMiner extends BlockMiner<RepuBlockCreator> {


  //private static final String CONTRACT_FILENAME = "repuContractAddress";
private static final String CONTRACT_ADDRESS = "0x0000000000000000000000000000000000008888";
  private static final BigInteger GAS_PRICE = new BigInteger("500000");
  private static final BigInteger GAS_LIMIT = new BigInteger("3000000");
  private final Web3j web3j;
  private final NodeKey nodeKey;
  private static TestContractRepu testContract;
 // private static boolean contractDeployed = false;
  //private static boolean contractDeploying = false;
  private final Address localAddress;
  private final String httpUrl;
  private final String port;
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
    this.port = blockCreator.apply(parentHeader).getPort();
    this.httpUrl = "http://localhost:" + port;
    this.web3j = Web3j.build(new HttpService(httpUrl));
    try{
      if (testContract == null) getRepuContract();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected boolean mineBlock() throws Exception {
    LOG.warn("I am "+localAddress.toString()+" with port "+port);
    /*if ((!contractDeployed
            && RepuHelpers.addressIsAllowedToProduceNextBlock(localAddress, protocolContext, parentHeader)) ||
        (contractDeployed
            && localAddress.toString().equals(testContract.nextValidator()))) {*/
    if (RepuHelpers.addressIsAllowedToProduceNextBlock(localAddress, protocolContext, parentHeader)) {

      boolean mined = super.mineBlock();

      /*if (!contractDeployed && !contractDeploying)
        deployRepuContract();*/
      /*if(testContract != null) {
        LOG.info("Count: "+ testContract.getCount() +" Number: "+testContract.getNumber());
        testContract.incrementCount();
      }*/
      if(testContract != null) {
        LOG.info("Next validator: "+ testContract.nextValidator());
        //testContract.updateValidator(getNextValidator());
      }
      return mined;
    }

    return true; // terminate mining.
  }

  /*public String getNextValidator(){
    List<String> list = Arrays.asList("0xdbe8422e428429e59c604a0ae614629b7794b924", "0x2ed64d60e50f820b240eb5905b0a73848b2506d6");
    if(localAddress.toString().equals(list.get(0))) return list.get(1);
    else return list.get(0);
  }*/

  public void getRepuContract()  {
/*
    Path contractPath = getContractPath();
    if(contractPath.toFile().exists() && parentHeader.getNumber() > 1){
      contractDeployed = true;
      String address = readAddress(contractPath)*/;
      testContract = new TestContractRepu(CONTRACT_ADDRESS, web3j, getCredentials(), new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
      /*LOG.info("Detected consensus contract in address {}",testContract.getContractAddress());
    }
    else{
      testContract = null;
      contractDeployed = false;
    }*/
  }
/*
  public void deployRepuContract() throws Exception {
    Path contractPath = getContractPath();
    contractDeploying = true;

    testContract = TestContractRepu.deploy(web3j, getCredentials(),
            new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();

    Files.write(contractPath, testContract.getContractAddress().getBytes(UTF_8), StandardOpenOption.CREATE);
    LOG.info("Deployed consensus contract with address {}",testContract.getContractAddress());

    contractDeployed = true;
    contractDeploying =false;
  }*/

/*  private String readAddress (final Path path) throws IOException {
    final List<String> info = Files.readAllLines(path);
    if (info.size() != 1)
      throw new IllegalArgumentException("Consensus contract address file has a invalid format.");
    return info.get(0);
  }
*/
  public Credentials getCredentials(){
    return Credentials.create(nodeKey.getPrivateKey().getKey(),nodeKey.getPublicKey().toString());
  }

 /* public Path getContractPath() throws IOException {
    return Paths.get(new File("./data").getCanonicalPath())
              .resolve(CONTRACT_FILENAME);
  }*/

}
