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

import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.core.AddressHelpers;
import org.hyperledger.besu.ethereum.core.BlockHeaderTestFixture;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class RepuValidatorSelectorTest {

  private final List<Address> validatorList =
      Arrays.asList(
          AddressHelpers.ofValue(1),
          AddressHelpers.ofValue(2),
          AddressHelpers.ofValue(3),
          AddressHelpers.ofValue(4));

  @Test
  public void proposerForABlockIsBasedOnModBlockNumber() {
    final BlockHeaderTestFixture headerBuilderFixture = new BlockHeaderTestFixture();

    for (int prevBlockNumber = 0; prevBlockNumber < 10; prevBlockNumber++) {
      headerBuilderFixture.number(prevBlockNumber);
      final RepuValidatorSelector selector = new RepuValidatorSelector();
      final Address nextProposer =
          selector.selectValidatorForNextBlock(headerBuilderFixture.buildHeader());
      assertThat(nextProposer)
          .isEqualTo(validatorList.get((prevBlockNumber + 1) % validatorList.size()));
    }
  }
}
