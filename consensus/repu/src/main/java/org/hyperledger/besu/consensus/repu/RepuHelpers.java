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
import org.hyperledger.besu.consensus.repu.blockcreation.RepuBlockMiner;
import org.hyperledger.besu.consensus.repu.blockcreation.RepuValidatorSelector;
import org.hyperledger.besu.consensus.repu.contracts.ProxyContract;
import org.hyperledger.besu.consensus.repu.contracts.RepuContract;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static final Logger LOG = LoggerFactory.getLogger(RepuBlockMiner.class);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(500000);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000);
    private static final String VOTE_FILE = "validatorVote";
    public static final Integer VOTING_ROUND = 5;
    private static Web3j web3j;
    private static NodeKey nodeKey;
    private static ProxyContract proxyContract;
    public static RepuContract repuContract;
    private static Boolean contractDeployed = false;
    private static Boolean contractDeploying = false;
    private static Boolean voting = false;
    public static Map<String, String> validations = Stream.of(new String[][]{
            {"1", RepuContract.INITIAL_VALIDATOR},
            {"2", RepuContract.INITIAL_VALIDATOR},
            {"3", RepuContract.INITIAL_VALIDATOR},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static Address getValidatorOfBlock(final BlockHeader header) {
        final RepuExtraData extraData = RepuExtraData.decode(header);
        return extraData.getProposerAddress();
    }

    static Address getValidatorForBlockAfter(final BlockHeader parent) {
        final RepuValidatorSelector validatorSelector = new RepuValidatorSelector();
        return validatorSelector.selectValidatorForNextBlock(parent);
    }

    public static List<Address> getValidators() {
        final List<Address> validators = new ArrayList<>();

        if (repuContract == null) {
            validators.add(Address.fromHexString(RepuContract.INITIAL_VALIDATOR));
        } else {
            try {
                validators.addAll(repuContract.getValidators().stream().map(Address::fromHexString).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return validators;
    }

    static boolean isValidator(final Address candidate) {
        try {
            return repuContract != null
                    ? repuContract.isValidator(String.valueOf(candidate))
                    : candidate.toString().equals(RepuContract.INITIAL_VALIDATOR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("BusyWait")
    public static boolean addressIsAllowedToProduceNextBlock(final Address candidate, final BlockHeader parent) {
        try {
            if (repuContract != null && repuContract.getBlock() > parent.getNumber()) return false;

            while (!validations.containsKey(String.valueOf(parent.getNumber() + 1))) {
                updateList(parent.getNumber());
                Thread.sleep(100);
            }

            if (!isValidator(candidate)) return false;

            if (Objects.equals(candidate.toString(), validations.get(String.valueOf(parent.getNumber() + 1)))) {
                return true;
            } else {
                /*List<String> nextValidators = repuContract.nextValidators();
                long lastTimestamp = parent.getTimestamp() * 1000;
                int i = 0;
                while (true) {
                    if (System.currentTimeMillis() - lastTimestamp >= 30000) {
                        i++;
                        if (candidate.toString().equals(nextValidators.get(i % nextValidators.size()))) {
                            validations.put(String.valueOf(parent.getNumber() + 1), candidate.toString());
                            return true;
                        }
                    } else if (repuContract.getBlock() > parent.getNumber()) {
                        break;
                    } else {
                        Thread.sleep(1000);
                    }
                }*/
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void nextTurnAndVote(final long block, final String voterAddress) throws Exception {
        if (repuContract != null) {
            if ((block + 1) % VOTING_ROUND == 0 && !voting) {
                voting = true;
                Path votePath = Paths.get(new File("./data").getCanonicalPath()).resolve(VOTE_FILE);
                String address = readFile(votePath);
                if (!StringUtil.isNullOrEmpty(address)) {
                    LOG.info("Voting " + address);
                    repuContract.nextTurnAndVote(address, getNonce(voterAddress));
                    voting = false;
                } else {
                    voting = false;
                    repuContract.nextTurn();

                }
            } else if ((block + 1) % VOTING_ROUND != 0) {
                voting = false;
                repuContract.nextTurn();
            }
        }
    }

    public static void voteValidator(final long block, final String voterAddress) throws Exception {
        if (repuContract != null) {
            if ((block + 1) % VOTING_ROUND == 0 && !voting) {
                voting = true;
                Path votePath = Paths.get(new File("./data").getCanonicalPath()).resolve(VOTE_FILE);
                String address = readFile(votePath);
                if (!StringUtil.isNullOrEmpty(address)) {
                    LOG.info("Voting " + address);
                    repuContract.voteValidator(address, getNonce(voterAddress));
                    voting = false;
                }
            } else if ((block + 1) % VOTING_ROUND != 0) voting = false;
        }

    }

    public static void updateList(final long block) {
        try {
            if (repuContract != null) {
                validations.put(String.valueOf(block + 1), repuContract.nextValidators().get(0));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getRepuContract(final long block) {
        if (repuContract == null) {
            try {
                if (!contractDeploying && block > 2) {
                    contractDeployed = true;

                    proxyContract = new ProxyContract(web3j, getCredentials(),
                            new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
                    repuContract = new RepuContract(proxyContract.getConsensusAddress(), web3j, getCredentials(),
                            new StaticGasProvider(GAS_PRICE, GAS_LIMIT), proxyContract);

                    validations.put(String.valueOf(block + 1), repuContract.nextValidators().get(0));

                    LOG.info("Detected consensus contract in address {}", proxyContract.getConsensusAddress());
                } else {
                    contractDeployed = false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deployContracts(final long block) throws Exception {
        contractDeploying = true;

        proxyContract = ProxyContract.deploy(web3j, getCredentials(),
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        repuContract = RepuContract.deploy(web3j, getCredentials(),
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        repuContract.setProxyContract(proxyContract);

        validations.put(String.valueOf(block), repuContract.nextValidators().get(0));

        LOG.info("Deployed proxy contract into {} and consensus contract into {}",
                proxyContract.getContractAddress(), repuContract.getContractAddress());

        contractDeployed = true;
        contractDeploying = false;
    }

    public static void checkContractsAreDeployed(final long block) throws Exception {
        if (!contractDeployed && !contractDeploying)
            deployContracts(block);
    }

    @Nullable
    private static String readFile(@NotNull final Path path) throws IOException {
        if (path.toFile().exists()) {
            final List<String> info = Files.readAllLines(path);
            if (info.size() == 0) return null;
            else if (info.size() > 1) throw new IllegalArgumentException("ValidatorVote file has a invalid format.");
            else return info.get(0);
        }
        return null;
    }

    @NotNull
    public static BigInteger getNonce(final String address) {
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            return nonce.add(BigInteger.valueOf(1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Credentials getCredentials() {
        return Credentials.create(nodeKey.getPrivateKey().getKey(), nodeKey.getPublicKey().toString());
    }

    public static void printInfo(final long block, final String address) {
        try {
            if (repuContract != null) {
                if (block % VOTING_ROUND == 0) {
                    LOG.info("Votes count: " + repuContract.getVotes(address));
                } else if ((block - 1) % VOTING_ROUND == 0) {
                    if (isValidator(Address.fromHexString(address))) {
                        LOG.info("I am a validator. My reputation is: " + repuContract.getReputation(address));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setNodeKey(final NodeKey nodeKey) {
        RepuHelpers.nodeKey = nodeKey;
    }

    public static void setWeb3j(final Web3j web3j) {
        RepuHelpers.web3j = web3j;
    }
}
