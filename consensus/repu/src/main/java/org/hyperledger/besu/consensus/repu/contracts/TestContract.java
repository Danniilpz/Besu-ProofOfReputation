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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
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
public class TestContract extends Contract {
  public static final String BINARY =
      "608060405234801561001057600080fd5b506040516102cb3803806102cb8339818101604052810190610032919061007a565b80600081905550506100a7565b600080fd5b6000819050919050565b61005781610044565b811461006257600080fd5b50565b6000815190506100748161004e565b92915050565b6000602082840312156100905761008f61003f565b5b600061009e84828501610065565b91505092915050565b610215806100b66000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c8063a87d942c14610051578063d14e62b81461006f578063e5071b8e1461008b578063f2c9ecd814610095575b600080fd5b6100596100b3565b6040516100669190610103565b60405180910390f35b6100896004803603810190610084919061014f565b6100bc565b005b6100936100c6565b005b61009d6100e1565b6040516100aa9190610103565b60405180910390f35b60008054905090565b8060008190555050565b60016000808282546100d891906101ab565b92505081905550565b60006022905090565b6000819050919050565b6100fd816100ea565b82525050565b600060208201905061011860008301846100f4565b92915050565b600080fd5b61012c816100ea565b811461013757600080fd5b50565b60008135905061014981610123565b92915050565b6000602082840312156101655761016461011e565b5b60006101738482850161013a565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006101b6826100ea565b91506101c1836100ea565b92508282019050808211156101d9576101d861017c565b5b9291505056fea2646970667358221220821e7a32d43ab211f1575b290cef4eb9d9ab768512637e43e841f19641a2504764736f6c63430008120033";

  public static final String FUNC_SETCOUNT = "setCount";

  public static final String FUNC_INCREMENTCOUNT = "incrementCount";

  public static final String FUNC_GETCOUNT = "getCount";

  public static final String FUNC_GETNUMBER = "getNumber";

  public TestContract(
      final String contractAddress,
      final Web3j web3j,
      final TransactionManager transactionManager,
      final ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
  }

  public TestContract(
          String contractAddress,
          Web3j web3j,
          Credentials credentials,
          ContractGasProvider contractGasProvider) {
    super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
  }

  public RemoteFunctionCall<TransactionReceipt> setCount(BigInteger count) {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_SETCOUNT,
                    Arrays.asList(new Uint256(count)),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<TransactionReceipt> incrementCount() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_INCREMENTCOUNT,
                    Arrays.asList(),
                    Collections.emptyList());
    return executeRemoteCallTransaction(function);
  }

  public RemoteFunctionCall<BigInteger> getCount() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETCOUNT,
                    Arrays.asList(),
                    Arrays.asList(new TypeReference<Uint256>() {}));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public RemoteFunctionCall<BigInteger> getNumber() {
    final org.web3j.abi.datatypes.Function function =
        new org.web3j.abi.datatypes.Function(
            FUNC_GETNUMBER,
            Arrays.asList(),
            Arrays.asList(new TypeReference<Uint256>() {}));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
  }

  public static RemoteCall<TestContract> deploy(
          Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
    return deployRemoteCall(
            TestContract.class,
            web3j,
            credentials,
            contractGasProvider,
            BINARY,
            FunctionEncoder.encodeConstructor(Arrays.asList(new Uint256(0))));
  }
}
