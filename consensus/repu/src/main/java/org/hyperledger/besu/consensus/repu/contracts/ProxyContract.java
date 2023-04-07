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
    public static final String BINARY = "608060405234801561001057600080fd5b506040516103f33803806103f3833981810160405281019061003291906100db565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610108565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006100a88261007d565b9050919050565b6100b88161009d565b81146100c357600080fd5b50565b6000815190506100d5816100af565b92915050565b6000602082840312156100f1576100f0610078565b5b60006100ff848285016100c6565b91505092915050565b6102dc806101176000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80638ed8f67f1461003b578063dddce8c114610057575b600080fd5b610055600480360381019061005091906101d2565b610075565b005b61005f610146565b60405161006c919061020e565b60405180910390f35b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610103576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016100fa90610286565b60405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061019f82610174565b9050919050565b6101af81610194565b81146101ba57600080fd5b50565b6000813590506101cc816101a6565b92915050565b6000602082840312156101e8576101e761016f565b5b60006101f6848285016101bd565b91505092915050565b61020881610194565b82525050565b600060208201905061022360008301846101ff565b92915050565b600082825260208201905092915050565b7f596f7520617265206e6f7420617574686f72697a656400000000000000000000600082015250565b6000610270601683610229565b915061027b8261023a565b602082019050919050565b6000602082019050818103600083015261029f81610263565b905091905056fea26469706673582212206f69f205f55dbbc8ea6ba8614432cc2e9fe5a5d7398a9002e09072a90c4d31b664736f6c63430008120033";
    public static final String FUNC_GETCONSENSUSADDRESS = "getConsensusAddress";
    //public static final String FUNC_SETCONSENSUSADDRESS = "setConsensusAddress";
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

    public RemoteFunctionCall<String> getConsensusAddressCall() {
        final org.web3j.abi.datatypes.Function function =
                new org.web3j.abi.datatypes.Function(
                        FUNC_GETCONSENSUSADDRESS,
                        Collections.emptyList(),
                        Collections.singletonList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<ProxyContract>





    deploy(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                ProxyContract.class,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                FunctionEncoder.encodeConstructor(Collections.singletonList(new Address(RepuContract.INITIAL_ADDRESS))));
    }

    public String getConsensusAddress() throws Exception { return this.getConsensusAddressCall().send(); }

}
