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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import java.util.Collections;

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
public class TestContractRepu extends Contract {
  public static final String BINARY =
      "608060405234801561001057600080fd5b50731c21335d5e5d3f675d7eb7e19e943535555bb2916000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506101d1806100746000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80633a5490461461003b578063635b277014610059575b600080fd5b610043610075565b6040516100509190610122565b60405180910390f35b610073600480360381019061006e919061016e565b61009e565b005b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061010c826100e1565b9050919050565b61011c81610101565b82525050565b60006020820190506101376000830184610113565b92915050565b600080fd5b61014b81610101565b811461015657600080fd5b50565b60008135905061016881610142565b92915050565b6000602082840312156101845761018361013d565b5b600061019284828501610159565b9150509291505056fea26469706673582212205a29f3bb5b21461e2cd545313c8d4363a54c99cd1736fabfcc92bce629f003a164736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATOR = "nextValidator";
  public static final String FUNC_UPDATEVALIDATOR = "updateValidator";
  public static String CONTRACT_ADDRESS = "0x9c406dfc7c68231087cdc4f02c246b65ff1557b8";

  public TestContractRepu(
      final String contractAddress,
      final Web3j web3j,
      final TransactionManager transactionManager,
      final ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public TestContractRepu(
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
  }

  public TestContractRepu(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  public RemoteFunctionCall<TransactionReceipt> updateValidatorCall(String addr) {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_UPDATEVALIDATOR,
                    Collections.singletonList(new Address(addr)),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<String> nextValidatorCall() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_NEXTVALIDATOR,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Address>() {}));
    return executeRemoteCallSingleValueReturn(function, String.class);
  }

  public static RemoteCall<TestContractRepu> deploy(
          Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
    return deployRemoteCall(
            TestContractRepu.class,
            web3j,
            credentials,
            contractGasProvider,
            BINARY,
            FunctionEncoder.encodeConstructor(Collections.emptyList()));
  }

  public String nextValidator() throws Exception { return this.nextValidatorCall().send().toString(); }

  public void updateValidator(final String addr) throws Exception { this.updateValidatorCall(addr).send(); }

}
