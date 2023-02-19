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
package org.hyperledger.besu.tests.acceptance.dsl.condition.repu;

import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.tests.acceptance.dsl.WaitUtils;
import org.hyperledger.besu.tests.acceptance.dsl.condition.Condition;
import org.hyperledger.besu.tests.acceptance.dsl.node.Node;
import org.hyperledger.besu.tests.acceptance.dsl.transaction.repu.RepuTransactions;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpectValidatorsAtBlockHash implements Condition {
  private final RepuTransactions repu;
  private final Hash blockHash;
  private final Address[] validators;

  public ExpectValidatorsAtBlockHash(
      final RepuTransactions repu, final Hash blockHash, final Address... validators) {
    this.repu = repu;
    this.blockHash = blockHash;
    this.validators = validators;
  }

  @Override
  public void verify(final Node node) {
    WaitUtils.waitFor(
        () ->
            assertThat(node.execute(repu.createGetSignersAtHash(blockHash)))
                .containsExactly(validators));
  }
}
