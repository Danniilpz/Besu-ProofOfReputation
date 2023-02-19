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
package org.hyperledger.besu.tests.acceptance.dsl.transaction.repu;

import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.tests.acceptance.dsl.node.BesuNode;

public class RepuTransactions {
  public static final String LATEST = "latest";

  public RepuPropose createRemoveProposal(final BesuNode node) {
    return propose(node.getAddress().toString(), false);
  }

  public RepuPropose createAddProposal(final BesuNode node) {
    return propose(node.getAddress().toString(), true);
  }

  private RepuPropose propose(final String address, final boolean auth) {
    return new RepuPropose(address, auth);
  }

  public RepuProposals createProposals() {
    return new RepuProposals();
  }

  public RepuGetSigners createGetSigners(final String blockNumber) {
    return new RepuGetSigners(blockNumber);
  }

  public RepuGetSignersAtHash createGetSignersAtHash(final Hash blockHash) {
    return new RepuGetSignersAtHash(blockHash);
  }

  public RepuDiscard createDiscardProposal(final BesuNode node) {
    return new RepuDiscard(node.getAddress().toString());
  }
}
