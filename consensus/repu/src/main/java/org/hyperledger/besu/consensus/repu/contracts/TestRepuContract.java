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
package org.hyperledger.besu.consensus.repu.contracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Auto generated code.
 *
 * <p><strong>Do not modify!</strong>
 *
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the <a
 * href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
public class TestRepuContract extends Contract {
  public static final String BINARY = "608060405234801561001057600080fd5b50604051610c2d380380610c2d83398181016040528101906100329190610249565b6000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060018190555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610276565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610216826101eb565b9050919050565b6102268161020b565b811461023157600080fd5b50565b6000815190506102438161021d565b92915050565b60006020828403121561025f5761025e6101e6565b5b600061026d84828501610234565b91505092915050565b6109a8806102856000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632d497ba2146100675780634d238c8e14610071578063b7ab4db51461008d578063b9137cca146100ab578063bdf3e088146100c9578063ec556889146100e5575b600080fd5b61006f610103565b005b61008b600480360381019061008691906105f7565b61011d565b005b610095610183565b6040516100a291906106e2565b60405180910390f35b6100b3610211565b6040516100c091906106e2565b60405180910390f35b6100e360048036038101906100de91906105f7565b61040b565b005b6100ed61056e565b6040516100fa9190610713565b60405180910390f35b6001600081548092919061011690610767565b9190505550565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6060600080548060200260200160405190810160405280929190818152602001828054801561020757602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116101bd575b5050505050905090565b60606000808054905060015461022791906107de565b9050600080808054905067ffffffffffffffff81111561024a5761024961080f565b5b6040519080825280602002602001820160405280156102785781602001602082028036833780820191505090505b5090505b600080549050831015610336576000838154811061029d5761029c61083e565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168183815181106102db576102da61083e565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050828061032090610767565b935050818061032e90610767565b92505061027c565b600092505b60008054905060015461034e91906107de565b83101561040257600083815481106103695761036861083e565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168183815181106103a7576103a661083e565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505082806103ec90610767565b93505081806103fa90610767565b92505061033b565b80935050505090565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16836040516024016104589190610713565b6040516020818303038152906040527f8ed8f67f000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040516104e291906108de565b6000604051808303816000865af19150503d806000811461051f576040519150601f19603f3d011682016040523d82523d6000602084013e610524565b606091505b509150915081610569576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161056090610952565b60405180910390fd5b505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006105c482610599565b9050919050565b6105d4816105b9565b81146105df57600080fd5b50565b6000813590506105f1816105cb565b92915050565b60006020828403121561060d5761060c610594565b5b600061061b848285016105e2565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b610659816105b9565b82525050565b600061066b8383610650565b60208301905092915050565b6000602082019050919050565b600061068f82610624565b610699818561062f565b93506106a483610640565b8060005b838110156106d55781516106bc888261065f565b97506106c783610677565b9250506001810190506106a8565b5085935050505092915050565b600060208201905081810360008301526106fc8184610684565b905092915050565b61070d816105b9565b82525050565b60006020820190506107286000830184610704565b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000819050919050565b60006107728261075d565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036107a4576107a361072e565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b60006107e98261075d565b91506107f48361075d565b925082610804576108036107af565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b600081519050919050565b600081905092915050565b60005b838110156108a1578082015181840152602081019050610886565b60008484015250505050565b60006108b88261086d565b6108c28185610878565b93506108d2818560208601610883565b80840191505092915050565b60006108ea82846108ad565b915081905092915050565b600082825260208201905092915050565b7f4572726f72000000000000000000000000000000000000000000000000000000600082015250565b600061093c6005836108f5565b915061094782610906565b602082019050919050565b6000602082019050818103600083015261096b8161092f565b905091905056fea2646970667358221220159e685ee8a50d53cbb59c15b4d8a9642858cdba611c0d98a90845621b6d201964736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATORS = "nextValidators";
  public static final String FUNC_GETVALIDATORS = "getValidators";
  public static final String FUNC_ADDVALIDATOR = "addValidator";
  public static final String FUNC_UPDATEVALIDATOR = "updateValidators";
  public static String INITIAL_ADDRESS = "0xb624d87403ac9f170ea9678a07051adc6fd7dc16";
  private ProxyContract proxyContract;
  private static final Logger LOG = LoggerFactory.getLogger(TestRepuContract.class);

  public TestRepuContract(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider,
          ProxyContract proxyContract) {
    this(contractAddress, web3j, credentials, contractGasProvider);
    this.proxyContract = proxyContract;
  }

  public TestRepuContract(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  private RemoteFunctionCall<List> nextValidatorsCall() {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTVALIDATORS,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<DynamicArray<Address>>() {}));
    return new RemoteFunctionCall<List>(
            function,
            () -> {
              List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
              return convertToNative(result);
            });
  }

  private RemoteFunctionCall<List> getValidatorsCall() {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETVALIDATORS,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<DynamicArray<Address>>() {}));
    return new RemoteFunctionCall<List>(
            function,
            () -> {
              List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
              return convertToNative(result);
            });
  }

  private RemoteFunctionCall<TransactionReceipt> addValidatorCall(String address) {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_ADDVALIDATOR,
                    Collections.singletonList(new Address(address)),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  private RemoteFunctionCall<TransactionReceipt> updateValidatorCall() {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_UPDATEVALIDATOR,
                    Collections.emptyList(),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  public static RemoteCall<TestRepuContract> deploy(
          Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
    return deployRemoteCall(
            TestRepuContract.class,
            web3j,
            credentials,
            contractGasProvider,
            BINARY,
            FunctionEncoder.encodeConstructor(Collections.singletonList(new Address(ProxyContract.ADDRESS))));
  }

  public void setProxyContract(ProxyContract proxyContract) {
    this.proxyContract = proxyContract;
  }

  private void checkAddress(){
    if(!Objects.equals(proxyContract.getContractAddress(), this.getContractAddress())) {
      try {
        this.setContractAddress(proxyContract.getConsensusAddress());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public List<String> nextValidators() throws Exception { return this.nextValidatorsCall().send(); }

  public List<String> getValidators() throws Exception { return this.getValidatorsCall().send(); }

  public void updateValidator() throws Exception { this.updateValidatorCall().send(); }


}
