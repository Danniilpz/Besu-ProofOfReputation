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
package org.hyperledger.besu.consensus.repu.headervalidationrules;

import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderTestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(Parameterized.class)
public class VoteValidationRuleTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {RepuBlockInterface.DROP_NONCE, true},
          {RepuBlockInterface.ADD_NONCE, true},
          {0x01L, false},
          {0xFFFFFFFFFFFFFFFEL, false}
        });
  }

  @Parameter public long actualVote;

  @Parameter(1)
  public boolean expectedResult;

  @Test
  public void test() {
    final VoteValidationRule uut = new VoteValidationRule();
    final BlockHeaderTestFixture blockBuilder = new BlockHeaderTestFixture();
    blockBuilder.nonce(actualVote);

    final BlockHeader header = blockBuilder.buildHeader();

    assertThat(uut.validate(header, null)).isEqualTo(expectedResult);
  }
}
