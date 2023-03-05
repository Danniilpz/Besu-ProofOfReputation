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
public class ProxyContract extends Contract {
    public static final String BINARY =
            "608060405234801561001057600080fd5b50610333806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80638ed8f67f1461003b578063dddce8c114610057575b600080fd5b61005560048036038101906100509190610229565b610075565b005b61005f61019d565b60405161006c9190610265565b60405180910390f35b600073ffffffffffffffffffffffffffffffffffffffff1660008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16148061011b575060008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61015a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610151906102dd565b60405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006101f6826101cb565b9050919050565b610206816101eb565b811461021157600080fd5b50565b600081359050610223816101fd565b92915050565b60006020828403121561023f5761023e6101c6565b5b600061024d84828501610214565b91505092915050565b61025f816101eb565b82525050565b600060208201905061027a6000830184610256565b92915050565b600082825260208201905092915050565b7f596f7520617265206e6f74206175746f72697a65640000000000000000000000600082015250565b60006102c7601583610280565b91506102d282610291565b602082019050919050565b600060208201905081810360008301526102f6816102ba565b905091905056fea2646970667358221220bf6e9a3831acea5c911150dab6e45ba109a56ab0550b9cae3d82d1673eef936b64736f6c63430008120033";
    public static final String FUNC_GETCONSENSUSADDRESS = "getConsensusAddress";
    public static final String FUNC_SETCONSENSUSADDRESS = "setConsensusAddress";
    public static String CONTRACT_ADDRESS = "0x9c406dfc7c68231087cdc4f02c246b65ff1557b8";

    public ProxyContract(
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> setConsensusAddressCall(String address) {
        final org.web3j.abi.datatypes.Function function =
                new org.web3j.abi.datatypes.Function(
                        FUNC_SETCONSENSUSADDRESS,
                        Collections.singletonList(new Address(address)),
                        Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getConsensusAddressCall() {
        final org.web3j.abi.datatypes.Function function =
                new org.web3j.abi.datatypes.Function(
                        FUNC_GETCONSENSUSADDRESS,
                        Collections.emptyList(),
                        Collections.singletonList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<ProxyContract> deploy(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                ProxyContract.class,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                FunctionEncoder.encodeConstructor(Collections.emptyList()));
    }

    public String getConsensusAddress() throws Exception { return this.getConsensusAddressCall().send(); }

    public void setConsensusAddress(final String address) throws Exception { this.setConsensusAddressCall(address).send(); }

}
