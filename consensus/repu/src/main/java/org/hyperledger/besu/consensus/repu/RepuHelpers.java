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
package org.hyperledger.besu.consensus.repu;

import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuProposerSelector;
import org.hyperledger.besu.consensus.repu.contracts.ProxyContract;
import org.hyperledger.besu.consensus.repu.contracts.TestRepuContract;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepuHelpers {

    private static final BigInteger GAS_PRICE = new BigInteger("500000");
    private static final BigInteger GAS_LIMIT = new BigInteger("3000000");
    private static Web3j web3j;
    private static NodeKey nodeKey;
    private static ProxyContract proxyContract;
    public static TestRepuContract repuContract;
    private static boolean contractDeployed = false;
    private static boolean contractDeploying = false;
    private static final Logger LOG = LoggerFactory.getLogger(RepuHelpers.class);
    public static final String INITIAL_NODE_ADDRESS = "0x1c21335d5e5d3f675d7eb7e19e943535555bb291";
    public static Map<String, String> validations = Stream.of(new String[][]{
            {"1", INITIAL_NODE_ADDRESS},
            {"2", INITIAL_NODE_ADDRESS},
            {"3", INITIAL_NODE_ADDRESS},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static Address getProposerOfBlock(final BlockHeader header) {
        final RepuExtraData extraData = RepuExtraData.decode(header);
        return extraData.getProposerAddress();
    }

    static Address getProposerForBlockAfter(
            final BlockHeader parent, final ValidatorProvider validatorProvider) {
        final RepuProposerSelector proposerSelector = new RepuProposerSelector(validatorProvider);
        return proposerSelector.selectProposerForNextBlock(parent);
    }

    static boolean isSigner(final Address candidate) {
        List<String> validators;
        try {
            validators = repuContract.getValidators();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return validators.contains(candidate.toString());
    }

    public static List<Address> getValidators() {
        final List<Address> validators = new ArrayList<>();

        if (RepuHelpers.repuContract == null) {
            validators.add(Address.fromHexString(RepuHelpers.INITIAL_NODE_ADDRESS));
        } else {
            try {
                validators.addAll(RepuHelpers.repuContract.getValidators().stream().map(Address::fromHexString).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return validators;
    }

    public static boolean addressIsAllowedToProduceNextBlock(final Address candidate, final ProtocolContext protocolContext, final BlockHeader parent) {
        while (!validations.containsKey(String.valueOf(parent.getNumber() + 1))) {
            updateList(parent);
        }

        //LOG.info(validations.get(String.valueOf(parent.getNumber() + 1)) + " will validate block #" + (parent.getNumber() + 1));
        return Objects.equals(candidate.toString(), validations.get(String.valueOf(parent.getNumber() + 1)));
    }

    public static void setNodeKey(NodeKey nodeKey) {
        RepuHelpers.nodeKey = nodeKey;
    }

    public static void setWeb3j(Web3j web3j) {
        RepuHelpers.web3j = web3j;
    }

    public static void getRepuContract(BlockHeader parentHeader) {
        if (repuContract == null) {
            try {
                if (!contractDeploying && parentHeader.getNumber() > 2) {
                    contractDeployed = true;

                    proxyContract = new ProxyContract(web3j, getCredentials(),
                            new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
                    repuContract =new TestRepuContract(proxyContract.getConsensusAddress(), web3j, getCredentials(),
                            new StaticGasProvider(GAS_PRICE, GAS_LIMIT), proxyContract);

                    validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidator());

                    //LOG.info("Detected consensus contract in address {}", proxyContract.getConsensusAddress());
                    //LOG.info("Validators: "+ repuContract.getValidators().toString());
                } else {
                    repuContract = null;
                    contractDeployed = false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deployContracts(BlockHeader parentHeader) throws Exception {
        contractDeploying = true;

        proxyContract = ProxyContract.deploy(web3j, getCredentials(),
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        repuContract = TestRepuContract.deploy(web3j, getCredentials(),
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        repuContract.setProxyContract(proxyContract);

        validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidator());

        //LOG.info("Deployed proxy contract into {} and consensus contract into {}",
        //        proxyContract.getContractAddress(), repuContract.getContractAddress());

        contractDeployed = true;
        contractDeploying = false;
    }

    public static void checkContractsAreDeployed(BlockHeader parent) throws Exception {
        if (!contractDeployed && !contractDeploying)
            deployContracts(parent);
    }

    public static void updateValidator() throws Exception {
        if (repuContract != null) {
            repuContract.updateValidator();
        }
    }

    public static Credentials getCredentials() {
        return Credentials.create(nodeKey.getPrivateKey().getKey(), nodeKey.getPublicKey().toString());
    }


    public static void updateList(BlockHeader parentHeader) {
        try {
            if (repuContract != null) {
                validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidator());
                //LOG.info("Next validator: " + nextValidator);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //LOG.info(validations.toString());
    }
}
