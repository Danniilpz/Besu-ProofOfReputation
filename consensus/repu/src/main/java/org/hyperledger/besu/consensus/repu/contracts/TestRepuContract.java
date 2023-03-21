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
  public static final String BINARY = "608060405234801561001057600080fd5b506000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600181905550739c406dfc7c68231087cdc4f02c246b65ff1557b8600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506107d6806101e26000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632d497ba2146100675780633a549046146100715780634d238c8e1461008f578063b7ab4db5146100ab578063bdf3e088146100c9578063ec556889146100e5575b600080fd5b61006f610103565b005b61007961011d565b6040516100869190610432565b60405180910390f35b6100a960048036038101906100a4919061047e565b610174565b005b6100b36101da565b6040516100c09190610569565b60405180910390f35b6100e360048036038101906100de919061047e565b610268565b005b6100ed6103cb565b6040516100fa9190610432565b60405180910390f35b60016000815480929190610116906105c4565b9190505550565b600080600080549050600154610133919061063b565b815481106101445761014361066c565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6060600080548060200260200160405190810160405280929190818152602001828054801561025e57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610214575b5050505050905090565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16836040516024016102b59190610432565b6040516020818303038152906040527f8ed8f67f000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff838183161783525050505060405161033f919061070c565b6000604051808303816000865af19150503d806000811461037c576040519150601f19603f3d011682016040523d82523d6000602084013e610381565b606091505b5091509150816103c6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103bd90610780565b60405180910390fd5b505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061041c826103f1565b9050919050565b61042c81610411565b82525050565b60006020820190506104476000830184610423565b92915050565b600080fd5b61045b81610411565b811461046657600080fd5b50565b60008135905061047881610452565b92915050565b6000602082840312156104945761049361044d565b5b60006104a284828501610469565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6104e081610411565b82525050565b60006104f283836104d7565b60208301905092915050565b6000602082019050919050565b6000610516826104ab565b61052081856104b6565b935061052b836104c7565b8060005b8381101561055c57815161054388826104e6565b975061054e836104fe565b92505060018101905061052f565b5085935050505092915050565b60006020820190508181036000830152610583818461050b565b905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000819050919050565b60006105cf826105ba565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036106015761060061058b565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b6000610646826105ba565b9150610651836105ba565b9250826106615761066061060c565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b600081519050919050565b600081905092915050565b60005b838110156106cf5780820151818401526020810190506106b4565b60008484015250505050565b60006106e68261069b565b6106f081856106a6565b93506107008185602086016106b1565b80840191505092915050565b600061071882846106db565b915081905092915050565b600082825260208201905092915050565b7f4572726f72000000000000000000000000000000000000000000000000000000600082015250565b600061076a600583610723565b915061077582610734565b602082019050919050565b600060208201905081810360008301526107998161075d565b905091905056fea2646970667358221220a623da53f0b40f136bab49bd9092a64a07989d2fa493b8917447130fee8ed90964736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATOR = "nextValidator";
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

  private RemoteFunctionCall<String> nextValidatorCall() {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTVALIDATOR,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Address>() {}));
    return executeRemoteCallSingleValueReturn(function, String.class);
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
            FunctionEncoder.encodeConstructor(Collections.emptyList()));
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
    LOG.info(this.getContractAddress());
  }

  public String nextValidator() throws Exception { return this.nextValidatorCall().send(); }

  public List<String> getValidators() throws Exception { return this.getValidatorsCall().send(); }

  public void updateValidator() throws Exception { this.updateValidatorCall().send(); }


}
