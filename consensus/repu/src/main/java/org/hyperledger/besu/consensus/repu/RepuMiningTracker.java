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

import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.core.BlockHeader;

public class RepuMiningTracker {

  private final Address localAddress;

  public RepuMiningTracker(final Address localAddress) {
    this.localAddress = localAddress;  }

  public boolean isProposerAfter(final BlockHeader header) {
    final Address nextProposer =
        RepuHelpers.getValidatorForBlockAfter(header);
    return localAddress.equals(nextProposer);
  }

  public boolean isSigner() {
    return RepuHelpers.isValidator(localAddress.toString());
  }

  public boolean canMakeBlockNextRound(final BlockHeader header) {
    return RepuHelpers.addressIsAllowedToProduceNextBlock(localAddress.toString(), header);
  }

  public boolean blockCreatedLocally(final BlockHeader header) {
    return RepuHelpers.getValidatorOfBlock(header).equals(localAddress);
  }
}
