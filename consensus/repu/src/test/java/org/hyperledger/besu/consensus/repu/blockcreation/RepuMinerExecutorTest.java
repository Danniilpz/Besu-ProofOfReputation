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
package org.hyperledger.besu.consensus.repu.blockcreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hyperledger.besu.config.GenesisConfigFile;
import org.hyperledger.besu.config.GenesisConfigOptions;
import org.hyperledger.besu.consensus.repu.RepuBlockHeaderFunctions;
import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.consensus.repu.RepuContext;
import org.hyperledger.besu.consensus.repu.RepuExtraData;
import org.hyperledger.besu.consensus.repu.RepuProtocolSchedule;
import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.crypto.NodeKey;
import org.hyperledger.besu.crypto.NodeKeyUtils;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.core.AddressHelpers;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderTestFixture;
import org.hyperledger.besu.ethereum.core.MiningParameters;
import org.hyperledger.besu.ethereum.core.Util;
import org.hyperledger.besu.ethereum.eth.transactions.ImmutableTransactionPoolConfiguration;
import org.hyperledger.besu.ethereum.eth.transactions.sorter.GasPricePendingTransactionsSorter;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.metrics.noop.NoOpMetricsSystem;
import org.hyperledger.besu.plugin.services.MetricsSystem;
import org.hyperledger.besu.testutil.TestClock;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.google.common.collect.Lists;
import org.apache.tuweni.bytes.Bytes;
import org.junit.Before;
import org.junit.Test;

public class RepuMinerExecutorTest {

  private static final int EPOCH_LENGTH = 10;
  private static final GenesisConfigOptions GENESIS_CONFIG_OPTIONS =
      GenesisConfigFile.fromConfig("{}").getConfigOptions();
  private final NodeKey proposerNodeKey = NodeKeyUtils.generate();
  private final Random random = new Random(21341234L);
  private Address localAddress;
  private final List<Address> validatorList = Lists.newArrayList();
  private ProtocolContext repuProtocolContext;
  private BlockHeaderTestFixture blockHeaderBuilder;
  private final MetricsSystem metricsSystem = new NoOpMetricsSystem();
  private final RepuBlockInterface blockInterface = new RepuBlockInterface();

  @Before
  public void setup() {
    localAddress = Util.publicKeyToAddress(proposerNodeKey.getPublicKey());
    validatorList.add(localAddress);
    validatorList.add(AddressHelpers.calculateAddressWithRespectTo(localAddress, 1));
    validatorList.add(AddressHelpers.calculateAddressWithRespectTo(localAddress, 2));
    validatorList.add(AddressHelpers.calculateAddressWithRespectTo(localAddress, 3));

    final ValidatorProvider validatorProvider = mock(ValidatorProvider.class);
    when(validatorProvider.getValidatorsAfterBlock(any())).thenReturn(validatorList);

    final RepuContext repuContext = new RepuContext(validatorProvider, null, blockInterface);
    repuProtocolContext = new ProtocolContext(null, null, repuContext);
    blockHeaderBuilder = new BlockHeaderTestFixture();
  }

  @Test
  public void extraDataCreatedOnEpochBlocksContainsValidators() {
    final Bytes vanityData = generateRandomVanityData();

    final RepuMinerExecutor executor =
        new RepuMinerExecutor(
            repuProtocolContext,
            RepuProtocolSchedule.create(
                GENESIS_CONFIG_OPTIONS, proposerNodeKey, false, EvmConfiguration.DEFAULT),
            new GasPricePendingTransactionsSorter(
                ImmutableTransactionPoolConfiguration.builder().txPoolMaxSize(1).build(),
                TestClock.system(ZoneId.systemDefault()),
                metricsSystem,
                RepuMinerExecutorTest::mockBlockHeader),
            proposerNodeKey,
            "8585",
            new MiningParameters.Builder()
                .coinbase(AddressHelpers.ofValue(1))
                .minTransactionGasPrice(Wei.ZERO)
                .extraData(vanityData)
                .miningEnabled(false)
                .build(),
            mock(RepuBlockScheduler.class),
            new EpochManager(EPOCH_LENGTH));

    // NOTE: Passing in the *parent* block, so must be 1 less than EPOCH
    final BlockHeader header = blockHeaderBuilder.number(EPOCH_LENGTH - 1).buildHeader();

    final Bytes extraDataBytes = executor.calculateExtraData(header);

    final RepuExtraData repuExtraData =
        RepuExtraData.decode(
            blockHeaderBuilder
                .number(EPOCH_LENGTH)
                .extraData(extraDataBytes)
                .blockHeaderFunctions(new RepuBlockHeaderFunctions())
                .buildHeader());

    assertThat(repuExtraData.getVanityData()).isEqualTo(vanityData);
    assertThat(repuExtraData.getValidators())
        .containsExactly(validatorList.toArray(new Address[0]));
  }

  @Test
  public void extraDataForNonEpochBlocksDoesNotContainValidaors() {
    final Bytes vanityData = generateRandomVanityData();

    final RepuMinerExecutor executor =
        new RepuMinerExecutor(
            repuProtocolContext,
            RepuProtocolSchedule.create(
                GENESIS_CONFIG_OPTIONS, proposerNodeKey, false, EvmConfiguration.DEFAULT),
            new GasPricePendingTransactionsSorter(
                ImmutableTransactionPoolConfiguration.builder().txPoolMaxSize(1).build(),
                TestClock.system(ZoneId.systemDefault()),
                metricsSystem,
                RepuMinerExecutorTest::mockBlockHeader),
            proposerNodeKey,
            "8585",
            new MiningParameters.Builder()
                .coinbase(AddressHelpers.ofValue(1))
                .minTransactionGasPrice(Wei.ZERO)
                .extraData(vanityData)
                .miningEnabled(false)
                .build(),
            mock(RepuBlockScheduler.class),
            new EpochManager(EPOCH_LENGTH));

    // Parent block was epoch, so the next block should contain no validators.
    final BlockHeader header = blockHeaderBuilder.number(EPOCH_LENGTH).buildHeader();

    final Bytes extraDataBytes = executor.calculateExtraData(header);

    final RepuExtraData repuExtraData =
        RepuExtraData.decode(
            blockHeaderBuilder
                .number(EPOCH_LENGTH)
                .extraData(extraDataBytes)
                .blockHeaderFunctions(new RepuBlockHeaderFunctions())
                .buildHeader());

    assertThat(repuExtraData.getVanityData()).isEqualTo(vanityData);
    assertThat(repuExtraData.getValidators()).isEqualTo(Lists.newArrayList());
  }

  @Test
  public void shouldUseLatestVanityData() {
    final Bytes initialVanityData = generateRandomVanityData();
    final Bytes modifiedVanityData = generateRandomVanityData();

    final RepuMinerExecutor executor =
        new RepuMinerExecutor(
            repuProtocolContext,
            RepuProtocolSchedule.create(
                GENESIS_CONFIG_OPTIONS, proposerNodeKey, false, EvmConfiguration.DEFAULT),
            new GasPricePendingTransactionsSorter(
                ImmutableTransactionPoolConfiguration.builder().txPoolMaxSize(1).build(),
                TestClock.system(ZoneId.systemDefault()),
                metricsSystem,
                RepuMinerExecutorTest::mockBlockHeader),
            proposerNodeKey,
            "8585",
            new MiningParameters.Builder()
                .coinbase(AddressHelpers.ofValue(1))
                .minTransactionGasPrice(Wei.ZERO)
                .extraData(initialVanityData)
                .miningEnabled(false)
                .build(),
            mock(RepuBlockScheduler.class),
            new EpochManager(EPOCH_LENGTH));

    executor.setExtraData(modifiedVanityData);
    final Bytes extraDataBytes = executor.calculateExtraData(blockHeaderBuilder.buildHeader());

    final RepuExtraData repuExtraData =
        RepuExtraData.decode(
            blockHeaderBuilder
                .number(EPOCH_LENGTH)
                .extraData(extraDataBytes)
                .blockHeaderFunctions(new RepuBlockHeaderFunctions())
                .buildHeader());
    assertThat(repuExtraData.getVanityData()).isEqualTo(modifiedVanityData);
  }

  private static BlockHeader mockBlockHeader() {
    final BlockHeader blockHeader = mock(BlockHeader.class);
    when(blockHeader.getBaseFee()).thenReturn(Optional.empty());
    return blockHeader;
  }

  private Bytes generateRandomVanityData() {
    final byte[] vanityData = new byte[32];
    random.nextBytes(vanityData);
    return Bytes.wrap(vanityData);
  }
}
