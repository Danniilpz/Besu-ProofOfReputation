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
            "608060405234801561001057600080fd5b506040516102e83803806102e8833981810160405281019061003291906100db565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610108565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006100a88261007d565b9050919050565b6100b88161009d565b81146100c357600080fd5b50565b6000815190506100d5816100af565b92915050565b6000602082840312156100f1576100f0610078565b5b60006100ff848285016100c6565b91505092915050565b6101d1806101176000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80638ed8f67f1461003b578063dddce8c114610057575b600080fd5b61005560048036038101906100509190610144565b610075565b005b61005f6100b8565b60405161006c9190610180565b60405180910390f35b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610111826100e6565b9050919050565b61012181610106565b811461012c57600080fd5b50565b60008135905061013e81610118565b92915050565b60006020828403121561015a576101596100e1565b5b60006101688482850161012f565b91505092915050565b61017a81610106565b82525050565b60006020820190506101956000830184610171565b9291505056fea26469706673582212205fb542ec3491c27ece65ef57898ed770bfc404568ab22b216a9ee42c90b8516964736f6c63430008120033000000000000000000000000b624d87403ac9f170ea9678a07051adc6fd7dc16";
    public static final String FUNC_GETCONSENSUSADDRESS = "getConsensusAddress";
    public static final String FUNC_SETCONSENSUSADDRESS = "setConsensusAddress";
    public static String ADDRESS = "0x9c406dfc7c68231087cdc4f02c246b65ff1557b8";

    public ProxyContract(
            final Web3j web3j,
            final Credentials credentials,
            final ContractGasProvider contractGasProvider) {
        super(BINARY, ADDRESS, web3j, credentials, contractGasProvider);
    }

    public ProxyContract(
            final String contractAddress,
            final Web3j web3j,
            final Credentials credentials,
            final ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
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
                FunctionEncoder.encodeConstructor(Collections.singletonList(new Address(TestRepuContract.INITIAL_ADDRESS))));
    }

    public String getConsensusAddress() throws Exception { return this.getConsensusAddressCall().send(); }

    public void setConsensusAddress(final String address) throws Exception { this.setConsensusAddressCall(address).send(); }

}
