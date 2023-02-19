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

import com.google.common.collect.Lists;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SignatureAlgorithmFactory;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.core.AddressHelpers;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderTestFixture;
import org.hyperledger.besu.ethereum.core.Util;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepuDifficultyCalculatorTest {

  private final KeyPair proposerKeyPair = SignatureAlgorithmFactory.getInstance().generateKeyPair();
  private Address localAddr;

  private final List<Address> validatorList = Lists.newArrayList();
  private ProtocolContext repuProtocolContext;
  private BlockHeaderTestFixture blockHeaderBuilder;
  private final RepuBlockInterface blockInterface = new RepuBlockInterface();

  @Before
  public void setup() {
    localAddr = Util.publicKeyToAddress(proposerKeyPair.getPublicKey());

    validatorList.add(localAddr);
    validatorList.add(AddressHelpers.calculateAddressWithRespectTo(localAddr, 1));

    final ValidatorProvider validatorProvider = mock(ValidatorProvider.class);
    when(validatorProvider.getValidatorsAfterBlock(any())).thenReturn(validatorList);

    final RepuContext repuContext = new RepuContext(validatorProvider, null, blockInterface);
    repuProtocolContext = new ProtocolContext(null, null, repuContext);
    blockHeaderBuilder = new BlockHeaderTestFixture();
  }

  @Test
  public void inTurnValidatorProducesDifficultyOfTwo() {
    final RepuDifficultyCalculator calculator = new RepuDifficultyCalculator(localAddr);

    final BlockHeader parentHeader = blockHeaderBuilder.number(1).buildHeader();

    assertThat(calculator.nextDifficulty(0, parentHeader, repuProtocolContext))
        .isEqualTo(BigInteger.valueOf(2));
  }

  @Test
  public void outTurnValidatorProducesDifficultyOfOne() {
    final RepuDifficultyCalculator calculator = new RepuDifficultyCalculator(localAddr);

    final BlockHeader parentHeader = blockHeaderBuilder.number(2).buildHeader();

    assertThat(calculator.nextDifficulty(0, parentHeader, repuProtocolContext))
        .isEqualTo(BigInteger.valueOf(1));
  }
}
