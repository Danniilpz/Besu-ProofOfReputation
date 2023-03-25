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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
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
  public static final String BINARY = "608060405234801561001057600080fd5b50604051610d15380380610d1583398181016040528101906100329190610251565b6000731c21335d5e5d3f675d7eb7e19e943535555bb2919080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000732ed64d60e50f820b240eb5905b0a73848b2506d69080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060007311f8ebff1b0ffb4de7814cc25430d01149fcdc719080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060018190555080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006003819055505061027e565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061021e826101f3565b9050919050565b61022e81610213565b811461023957600080fd5b50565b60008151905061024b81610225565b92915050565b600060208284031215610267576102666101ee565b5b60006102758482850161023c565b91505092915050565b610a888061028d6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063b9137cca1161005b578063b9137cca146100da578063bdf3e088146100f8578063d8c8beef14610114578063ec556889146101305761007d565b80634d238c8e146100825780637f2c4ca81461009e578063b7ab4db5146100bc575b600080fd5b61009c60048036038101906100979190610654565b61014e565b005b6100a66101b4565b6040516100b3919061069a565b60405180910390f35b6100c46101be565b6040516100d19190610773565b60405180910390f35b6100e261024c565b6040516100ef9190610773565b60405180910390f35b610112600480360381019061010d9190610654565b610446565b005b61012e600480360381019061012991906107c1565b6105a9565b005b6101386105cb565b60405161014591906107fd565b60405180910390f35b6000819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000600354905090565b6060600080548060200260200160405190810160405280929190818152602001828054801561024257602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116101f8575b5050505050905090565b6060600080805490506001546102629190610847565b9050600080808054905067ffffffffffffffff81111561028557610284610878565b5b6040519080825280602002602001820160405280156102b35781602001602082028036833780820191505090505b5090505b60008054905083101561037157600083815481106102d8576102d76108a7565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16818381518110610316576103156108a7565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050828061035b90610905565b935050818061036990610905565b9250506102b7565b600092505b6000805490506001546103899190610847565b83101561043d57600083815481106103a4576103a36108a7565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168183815181106103e2576103e16108a7565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050828061042790610905565b935050818061043590610905565b925050610376565b80935050505090565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168360405160240161049391906107fd565b6040516020818303038152906040527f8ed8f67f000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff838183161783525050505060405161051d91906109be565b6000604051808303816000865af19150503d806000811461055a576040519150601f19603f3d011682016040523d82523d6000602084013e61055f565b606091505b5091509150816105a4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161059b90610a32565b60405180910390fd5b505050565b600160008154809291906105bc90610905565b91905055508060038190555050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610621826105f6565b9050919050565b61063181610616565b811461063c57600080fd5b50565b60008135905061064e81610628565b92915050565b60006020828403121561066a576106696105f1565b5b60006106788482850161063f565b91505092915050565b6000819050919050565b61069481610681565b82525050565b60006020820190506106af600083018461068b565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6106ea81610616565b82525050565b60006106fc83836106e1565b60208301905092915050565b6000602082019050919050565b6000610720826106b5565b61072a81856106c0565b9350610735836106d1565b8060005b8381101561076657815161074d88826106f0565b975061075883610708565b925050600181019050610739565b5085935050505092915050565b6000602082019050818103600083015261078d8184610715565b905092915050565b61079e81610681565b81146107a957600080fd5b50565b6000813590506107bb81610795565b92915050565b6000602082840312156107d7576107d66105f1565b5b60006107e5848285016107ac565b91505092915050565b6107f781610616565b82525050565b600060208201905061081260008301846107ee565b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b600061085282610681565b915061085d83610681565b92508261086d5761086c610818565b5b828206905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600061091082610681565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203610942576109416108d6565b5b600182019050919050565b600081519050919050565b600081905092915050565b60005b83811015610981578082015181840152602081019050610966565b60008484015250505050565b60006109988261094d565b6109a28185610958565b93506109b2818560208601610963565b80840191505092915050565b60006109ca828461098d565b915081905092915050565b600082825260208201905092915050565b7f4572726f72000000000000000000000000000000000000000000000000000000600082015250565b6000610a1c6005836109d5565b9150610a27826109e6565b602082019050919050565b60006020820190508181036000830152610a4b81610a0f565b905091905056fea2646970667358221220c3b84d12f3615e2d23e087df3e6b1b24571bc84417a078b82b6687a7d0dd102b64736f6c63430008120033";
  public static final String FUNC_NEXTVALIDATORS = "nextValidators";
  public static final String FUNC_GETLASTBLOCK = "getLastBlock";
  public static final String FUNC_GETVALIDATORS = "getValidators";
  public static final String FUNC_ADDVALIDATOR = "addValidator";
  public static final String FUNC_UPDATEVALIDATORS = "updateValidators";
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

  private RemoteFunctionCall<BigInteger> getLastBlockCall() {
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_GETLASTBLOCK,
                    Collections.emptyList(),
                    Collections.singletonList(new TypeReference<Uint256>() {}));
    return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

  private RemoteFunctionCall<TransactionReceipt> updateValidatorsCall(BigInteger block) {
    this.checkAddress();
    final org.web3j.abi.datatypes.Function function =
            new org.web3j.abi.datatypes.Function(
                    FUNC_UPDATEVALIDATORS,
                    Collections.singletonList(new Uint256(block)),
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

  public Long getLastBlock() throws Exception { return this.getLastBlockCall().send().longValue(); }

  public List<String> getValidators() throws Exception { return this.getValidatorsCall().send(); }

  public void updateValidators(BigInteger block) throws Exception { this.updateValidatorsCall(block).send(); }


}
