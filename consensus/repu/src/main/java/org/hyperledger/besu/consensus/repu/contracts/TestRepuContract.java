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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
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
  public static final String BINARY = "608060405234801561001057600080fd5b506000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060046001819055506105558061018d6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80632d497ba21461005c5780633a5490461461006657806383788fce14610084578063b7ab4db5146100a2578063efb0b655146100c0575b600080fd5b6100646100dc565b005b61006e6100f6565b60405161007b919061028c565b60405180910390f35b61008c61014d565b60405161009991906102c0565b60405180910390f35b6100aa610157565b6040516100b79190610399565b60405180910390f35b6100da60048036038101906100d591906103ec565b6101e5565b005b600160008154809291906100ef90610448565b9190505550565b60008060008054905060015461010c91906104bf565b8154811061011d5761011c6104f0565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000600154905090565b606060008054806020026020016040519081016040528092919081815260200182805480156101db57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610191575b5050505050905090565b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006102768261024b565b9050919050565b6102868161026b565b82525050565b60006020820190506102a1600083018461027d565b92915050565b6000819050919050565b6102ba816102a7565b82525050565b60006020820190506102d560008301846102b1565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6103108161026b565b82525050565b60006103228383610307565b60208301905092915050565b6000602082019050919050565b6000610346826102db565b61035081856102e6565b935061035b836102f7565b8060005b8381101561038c5781516103738882610316565b975061037e8361032e565b92505060018101905061035f565b5085935050505092915050565b600060208201905081810360008301526103b3818461033b565b905092915050565b600080fd5b6103c98161026b565b81146103d457600080fd5b50565b6000813590506103e6816103c0565b92915050565b600060208284031215610402576104016103bb565b5b6000610410848285016103d7565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610453826102a7565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff820361048557610484610419565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b60006104ca826102a7565b91506104d5836102a7565b9250826104e5576104e4610490565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fdfea26469706673582212207beb4089ea08b367a9920df319a0e1199424c4d0161fb2e34a67a066c4c90bd764736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATOR = "nextValidator";
  public static final String FUNC_NEXTBLOCK = "nextBlock";
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

  private RemoteFunctionCall<BigInteger> nextBlockCall() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTBLOCK,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Uint256>() {}));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

  public BigInteger nextBlock() throws Exception { return this.nextBlockCall().send(); }

  public List<String> getValidators() throws Exception { return this.getValidatorsCall().send(); }

  public List<String> nextValidatorBlock() throws Exception { return Arrays.asList(String.valueOf(nextBlock()), nextValidator());}

  public void addValidator(final String addr) throws Exception { this.addValidatorCall(addr).send(); }

  public void updateValidator() throws Exception { this.updateValidatorCall().send(); }


}
