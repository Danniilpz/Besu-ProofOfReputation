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
  public static final String BINARY = "608060405234801561001057600080fd5b5060405161106838038061106883398181016040528101906100329190610251565b6000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060018190555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006003819055505061027e565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061021e826101f3565b9050919050565b61022e81610213565b811461023957600080fd5b50565b60008151905061024b81610225565b92915050565b600060208284031215610267576102666101ee565b5b60006102758482850161023c565b91505092915050565b610ddb8061028d6000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063b9137cca1161005b578063b9137cca14610104578063bdf3e08814610122578063d8c8beef1461013e578063ec5568891461015a57610088565b806343a73d9a1461008d5780634d238c8e146100ac5780637f2c4ca8146100c8578063b7ab4db5146100e6575b600080fd5b610095610178565b6040516100a3929190610846565b60405180910390f35b6100c660048036038101906100c191906108a0565b6101a8565b005b6100d061020e565b6040516100dd91906108e6565b60405180910390f35b6100ee610218565b6040516100fb91906109bf565b60405180910390f35b61010c6102a6565b60405161011991906109bf565b60405180910390f35b61013c600480360381019061013791906108a0565b6104a0565b005b61015860048036038101906101539190610a0d565b6107a2565b005b6101626107c4565b60405161016f9190610a3a565b60405180910390f35b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166001915091509091565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000600354905090565b6060600080548060200260200160405190810160405280929190818152602001828054801561029c57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610252575b5050505050905090565b6060600080805490506001546102bc9190610a84565b9050600080808054905067ffffffffffffffff8111156102df576102de610ab5565b5b60405190808252806020026020018201604052801561030d5781602001602082028036833780820191505090505b5090505b6000805490508310156103cb576000838154811061033257610331610ae4565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168183815181106103705761036f610ae4565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505082806103b590610b42565b93505081806103c390610b42565b925050610311565b600092505b6000805490506001546103e39190610a84565b83101561049757600083815481106103fe576103fd610ae4565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681838151811061043c5761043b610ae4565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050828061048190610b42565b935050818061048f90610b42565b9250506103d0565b80935050505090565b806000808273ffffffffffffffffffffffffffffffffffffffff166040516024016040516020818303038152906040527f43a73d9a000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff838183161783525050505060405161054b9190610bfb565b6000604051808303816000865af19150503d8060008114610588576040519150601f19603f3d011682016040523d82523d6000602084013e61058d565b606091505b5091509150600080828060200190518101906105a99190610c7c565b915091508173ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161461063d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161063490610d19565b60405180910390fd5b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16876040516024016106899190610a3a565b6040516020818303038152906040527f8ed8f67f000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040516107139190610bfb565b6000604051808303816000865af19150503d8060008114610750576040519150601f19603f3d011682016040523d82523d6000602084013e610755565b606091505b5050905080610799576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161079090610d85565b60405180910390fd5b50505050505050565b600160008154809291906107b590610b42565b91905055508060038190555050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610815826107ea565b9050919050565b6108258161080a565b82525050565b60008115159050919050565b6108408161082b565b82525050565b600060408201905061085b600083018561081c565b6108686020830184610837565b9392505050565b600080fd5b61087d8161080a565b811461088857600080fd5b50565b60008135905061089a81610874565b92915050565b6000602082840312156108b6576108b561086f565b5b60006108c48482850161088b565b91505092915050565b6000819050919050565b6108e0816108cd565b82525050565b60006020820190506108fb60008301846108d7565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6109368161080a565b82525050565b6000610948838361092d565b60208301905092915050565b6000602082019050919050565b600061096c82610901565b610976818561090c565b93506109818361091d565b8060005b838110156109b2578151610999888261093c565b97506109a483610954565b925050600181019050610985565b5085935050505092915050565b600060208201905081810360008301526109d98184610961565b905092915050565b6109ea816108cd565b81146109f557600080fd5b50565b600081359050610a07816109e1565b92915050565b600060208284031215610a2357610a2261086f565b5b6000610a31848285016109f8565b91505092915050565b6000602082019050610a4f600083018461081c565b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b6000610a8f826108cd565b9150610a9a836108cd565b925082610aaa57610aa9610a55565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610b4d826108cd565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203610b7f57610b7e610b13565b5b600182019050919050565b600081519050919050565b600081905092915050565b60005b83811015610bbe578082015181840152602081019050610ba3565b60008484015250505050565b6000610bd582610b8a565b610bdf8185610b95565b9350610bef818560208601610ba0565b80840191505092915050565b6000610c078284610bca565b915081905092915050565b6000610c1d826107ea565b9050919050565b610c2d81610c12565b8114610c3857600080fd5b50565b600081519050610c4a81610c24565b92915050565b610c598161082b565b8114610c6457600080fd5b50565b600081519050610c7681610c50565b92915050565b60008060408385031215610c9357610c9261086f565b5b6000610ca185828601610c3b565b9250506020610cb285828601610c67565b9150509250929050565b600082825260208201905092915050565b7f50726f78792061646472657373206973206e6f7420636f727265637400000000600082015250565b6000610d03601c83610cbc565b9150610d0e82610ccd565b602082019050919050565b60006020820190508181036000830152610d3281610cf6565b9050919050565b7f4572726f72000000000000000000000000000000000000000000000000000000600082015250565b6000610d6f600583610cbc565b9150610d7a82610d39565b602082019050919050565b60006020820190508181036000830152610d9e81610d62565b905091905056fea26469706673582212205c8ddf6b91bb49288ba7beb9b81c6efc52f12fc1d0a41fff2bbc054d96b7a79e64736f6c63430008120033";
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
