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

import io.netty.util.internal.StringUtil;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuProposerSelector;
import org.hyperledger.besu.consensus.repu.contracts.ProxyContract;
import org.hyperledger.besu.consensus.repu.contracts.RepuContract;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepuHelpers {

    private static final BigInteger GAS_PRICE = new BigInteger("500000");
    private static final BigInteger GAS_LIMIT = new BigInteger("3000000");
    private static final String VOTE_FILE = "ValidatorVote";
    private static Web3j web3j;
    private static NodeKey nodeKey;
    private static ProxyContract proxyContract;
    public static RepuContract repuContract;
    private static boolean contractDeployed = false;
    private static boolean contractDeploying = false;
    private static boolean voting = false;
    private static final Logger LOG = LoggerFactory.getLogger(RepuHelpers.class);
    public static final String INITIAL_NODE_ADDRESS = "0x1c21335d5e5d3f675d7eb7e19e943535555bb291";
    public static Map<String, String> validations = Stream.of(new String[][]{
            {"1", RepuContract.INITIAL_VALIDATOR},
            {"2", RepuContract.INITIAL_VALIDATOR},
            {"3", RepuContract.INITIAL_VALIDATOR},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static Address getProposerOfBlock(final BlockHeader header) {
        final RepuExtraData extraData = RepuExtraData.decode(header);
        return extraData.getProposerAddress();
    }

    static Address getProposerForBlockAfter(
            final BlockHeader parent, final ValidatorProvider validatorProvider) {
        final RepuProposerSelector proposerSelector = new RepuProposerSelector();
        return proposerSelector.selectProposerForNextBlock(parent);
    }

    static boolean isSigner(final Address candidate) {
        try {
            return repuContract != null
                    ? repuContract.isValidator(String.valueOf(candidate))
                    : candidate.toString().equals(RepuContract.INITIAL_VALIDATOR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Address> getValidators() {
        final List<Address> validators = new ArrayList<>();

        if (RepuHelpers.repuContract == null) {
            validators.add(Address.fromHexString(RepuContract.INITIAL_VALIDATOR));
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

        if (!isSigner(candidate)) {
            return false;
        }
        //LOG.info(validations.get(String.valueOf(parent.getNumber() + 1)) + " will validate block #" + (parent.getNumber() + 1));
        return Objects.equals(candidate.toString(), validations.get(String.valueOf(parent.getNumber() + 1)));

        /*
        boolean isAllowed = Objects.equals(candidate.toString(), validations.get(String.valueOf(parent.getNumber() + 1)));

        if(!isAllowed && repuContract != null){
            long startTime = System.currentTimeMillis();
            try {
                while(repuContract.getLastBlock() != (parent.getNumber() + 1)
                        && ((System.currentTimeMillis() - startTime) < 20000)){}

                if(repuContract.getLastBlock() != (parent.getNumber() + 1)){
                    validatorIndex++;
                    updateList(parent);
                    isAllowed = Objects.equals(candidate.toString(), validations.get(String.valueOf(parent.getNumber() + 1)));
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //LOG.info(validations.get(String.valueOf(parent.getNumber() + 1)) + " will validate block #" + (parent.getNumber() + 1));
        return isAllowed;
         */
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
                    repuContract = new RepuContract(proxyContract.getConsensusAddress(), web3j, getCredentials(),
                            new StaticGasProvider(GAS_PRICE, GAS_LIMIT), proxyContract);

                    validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidators().get(0));

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
        repuContract = RepuContract.deploy(web3j, getCredentials(),
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        repuContract.setProxyContract(proxyContract);

        validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidators().get(0));

        //LOG.info("Deployed proxy contract into {} and consensus contract into {}",
        //        proxyContract.getContractAddress(), repuContract.getContractAddress());

        contractDeployed = true;
        contractDeploying = false;
    }

    public static void checkContractsAreDeployed(BlockHeader parent) throws Exception {
        if (!contractDeployed && !contractDeploying)
            deployContracts(parent);
    }

    public static void nextTurn() throws Exception {
        if (repuContract != null) {
            repuContract.nextTurn();
        }
    }

    public static void voteValidator(long block, String voterAddress) throws Exception {
        if (repuContract != null) {
            if (block % 5 == 0 && !voting) {
                voting = true;
                Path votePath = Paths.get(new File("./data").getCanonicalPath()).resolve(VOTE_FILE);
                String address = readFile(votePath);
                if (!StringUtil.isNullOrEmpty(address)) {
                    repuContract.voteValidator(address, getNonce(voterAddress));
                    voting = false;
                }
            } else if (block % 5 != 0) voting = false;
        }

    }

    private static String readFile(final Path path) throws IOException {
        if (path.toFile().exists()) {
            final List<String> info = Files.readAllLines(path);
            if (info.size() != 1)
                throw new IllegalArgumentException("ValidatorVote file has a invalid format.");
            return info.get(0);
        }
        return null;
    }

    public static Credentials getCredentials() {
        return Credentials.create(nodeKey.getPrivateKey().getKey(), nodeKey.getPublicKey().toString());
    }


    public static void updateList(BlockHeader parentHeader) {
        try {
            if (repuContract != null) {
                validations.put(String.valueOf(parentHeader.getNumber() + 1), repuContract.nextValidators().get(0));
                //LOG.info("Next validator: " + nextValidator);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //LOG.info(validations.toString());
    }

    public static BigInteger getNonce(String address) {
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            return nonce;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
