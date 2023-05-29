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
package org.hyperledger.besu.tests.acceptance.repu;

import org.hyperledger.besu.tests.acceptance.dsl.AcceptanceTestBase;
import org.hyperledger.besu.tests.acceptance.dsl.node.BesuNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hyperledger.besu.tests.acceptance.dsl.transaction.repu.RepuTransactions.LATEST;

@Ignore
public class RepuGetSignersRpcTest extends AcceptanceTestBase {
  private BesuNode minerNode1;
  private BesuNode minerNode2;

  @Before
  public void setUp() throws Exception {
    final String[] validators = {"miner1"};
    minerNode1 = besu.createCliqueNodeWithValidators("miner1", validators);
    minerNode2 = besu.createCliqueNodeWithValidators("miner2", validators);
    cluster.start(minerNode1, minerNode2);
  }

  @Test
  public void shouldBeAbleToGetValidatorsForBlockNumber() {
    cluster.verify(repu.validatorsAtBlockEqual("0x0", minerNode1));
    minerNode1.verify(blockchain.minimumHeight(1));

    minerNode1.execute(repuTransactions.createAddProposal(minerNode2));
    cluster.verify(blockchain.reachesHeight(minerNode1, 1));
    cluster.verify(repu.validatorsAtBlockEqual("0x2", minerNode1, minerNode2));
    cluster.verify(repu.validatorsAtBlockEqual(LATEST, minerNode1, minerNode2));
  }

  @Test
  public void shouldBeAbleToGetValidatorsForBlockHash() {
    cluster.verify(repu.validatorsAtBlockHashFromBlockNumberEqual(minerNode1, 0, minerNode1));
    minerNode1.verify(blockchain.minimumHeight(1));

    minerNode1.execute(repuTransactions.createAddProposal(minerNode2));
    cluster.verify(blockchain.reachesHeight(minerNode1, 1));
    cluster.verify(
        repu.validatorsAtBlockHashFromBlockNumberEqual(minerNode1, 2, minerNode1, minerNode2));
  }
}
