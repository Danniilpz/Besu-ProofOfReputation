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
  public static final String BINARY = "608060405234801561001057600080fd5b506000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600160006101000a81548160ff021916908360ff1602179055506105168061012a6000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80633a549046146100515780634d238c8e1461006f578063b7ab4db51461008b578063d0e53ce7146100a9575b600080fd5b6100596100b3565b6040516100669190610289565b60405180910390f35b610089600480360381019061008491906102d5565b61011a565b005b610093610180565b6040516100a091906103c0565b60405180910390f35b6100b161020e565b005b600080600080549050600160009054906101000a900460ff1660ff166100d9919061041b565b815481106100ea576100e961044c565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6060600080548060200260200160405190810160405280929190818152602001828054801561020457602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116101ba575b5050505050905090565b6001600081819054906101000a900460ff168092919061022d906104b7565b91906101000a81548160ff021916908360ff16021790555050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061027382610248565b9050919050565b61028381610268565b82525050565b600060208201905061029e600083018461027a565b92915050565b600080fd5b6102b281610268565b81146102bd57600080fd5b50565b6000813590506102cf816102a9565b92915050565b6000602082840312156102eb576102ea6102a4565b5b60006102f9848285016102c0565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b61033781610268565b82525050565b6000610349838361032e565b60208301905092915050565b6000602082019050919050565b600061036d82610302565b610377818561030d565b93506103828361031e565b8060005b838110156103b357815161039a888261033d565b97506103a583610355565b925050600181019050610386565b5085935050505092915050565b600060208201905081810360008301526103da8184610362565b905092915050565b6000819050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b6000610426826103e2565b9150610431836103e2565b925082610441576104406103ec565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600060ff82169050919050565b60006104c2826104aa565b915060ff82036104d5576104d461047b565b5b60018201905091905056fea264697066735822122072b245e1976306a66450f788a73af383510e5b1786f76bc593b01ee304882a0764736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATOR = "nextValidator";
  public static final String FUNC_GETVALIDATORS = "getValidators";
  public static final String FUNC_ADDVALIDATOR = "addValidator";
  public static final String FUNC_UPDATEVALIDATOR = "updateValidator";
  public static String INITIAL_ADDRESS = "0xb624d87403ac9f170ea9678a07051adc6fd7dc16";

  public TestRepuContract(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  public RemoteFunctionCall<String> nextValidatorCall() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTVALIDATOR,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Address>() {}));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  public RemoteFunctionCall<List> getValidatorsCall() {
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

  public RemoteFunctionCall<TransactionReceipt> addValidatorCall(String address) {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_ADDVALIDATOR,
                    Collections.singletonList(new Address(address)),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<TransactionReceipt> updateValidatorCall() {
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

  public String nextValidator() throws Exception { return this.nextValidatorCall().send(); }

  public List<String> getValidators() throws Exception { return this.getValidatorsCall().send(); }

  public void addValidator(final String addr) throws Exception { this.addValidatorCall(addr).send(); }

  public void updateValidator() throws Exception { this.updateValidatorCall().send(); }


}
