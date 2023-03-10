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
  public static final String BINARY = "608060405234801561001057600080fd5b506000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600160006101000a81548160ff021916908360ff160217905550610516806101a16000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80632d497ba2146100515780633a5490461461005b578063b7ab4db514610079578063efb0b65514610097575b600080fd5b6100596100b3565b005b6100636100ed565b6040516100709190610289565b60405180910390f35b610081610154565b60405161008e9190610362565b60405180910390f35b6100b160048036038101906100ac91906103b5565b6101e2565b005b6001600081819054906101000a900460ff16809291906100d29061041e565b91906101000a81548160ff021916908360ff16021790555050565b600080600080549050600160009054906101000a900460ff1660ff166101139190610480565b81548110610124576101236104b1565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b606060008054806020026020016040519081016040528092919081815260200182805480156101d857602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001906001019080831161018e575b5050505050905090565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061027382610248565b9050919050565b61028381610268565b82525050565b600060208201905061029e600083018461027a565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6102d981610268565b82525050565b60006102eb83836102d0565b60208301905092915050565b6000602082019050919050565b600061030f826102a4565b61031981856102af565b9350610324836102c0565b8060005b8381101561035557815161033c88826102df565b9750610347836102f7565b925050600181019050610328565b5085935050505092915050565b6000602082019050818103600083015261037c8184610304565b905092915050565b600080fd5b61039281610268565b811461039d57600080fd5b50565b6000813590506103af81610389565b92915050565b6000602082840312156103cb576103ca610384565b5b60006103d9848285016103a0565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600060ff82169050919050565b600061042982610411565b915060ff820361043c5761043b6103e2565b5b600182019050919050565b6000819050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b600061048b82610447565b915061049683610447565b9250826104a6576104a5610451565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fdfea2646970667358221220766639569806540938ac74ca57c10c596a1048f66ee3525aec49de91b75222d364736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATOR = "nextValidator";
  public static final String FUNC_GETVALIDATORS = "getValidators";
  public static final String FUNC_ADDVALIDATOR = "addValidator";
  public static final String FUNC_UPDATEVALIDATOR = "updateValidators";
  public static String INITIAL_ADDRESS = "0xb624d87403ac9f170ea9678a07051adc6fd7dc16";

  public TestRepuContract(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  private RemoteFunctionCall<String> nextValidatorCall() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTVALIDATOR,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Address>() {}));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  private RemoteFunctionCall<List> getValidatorsCall() {
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
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_ADDVALIDATOR,
                    Collections.singletonList(new Address(address)),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  private RemoteFunctionCall<TransactionReceipt> updateValidatorCall() {
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
