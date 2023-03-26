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

import com.google.common.annotations.VisibleForTesting;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.blockcreation.DefaultBlockScheduler;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import java.time.Clock;

public class RepuBlockScheduler extends DefaultBlockScheduler {

    private final Address localNodeAddress;

    public RepuBlockScheduler(
            final Clock clock,
            final ValidatorProvider validatorProvider,
            final Address localNodeAddress,
            final long secondsBetweenBlocks) {
        super(secondsBetweenBlocks, 0L, clock);
        this.localNodeAddress = localNodeAddress;
    }

    @Override
    @VisibleForTesting
    public BlockCreationTimeResult getNextTimestamp(final BlockHeader parentHeader) {
        final BlockCreationTimeResult result = super.getNextTimestamp(parentHeader);

        final long milliSecondsUntilNextBlock =
                result.getMillisecondsUntilValid() + calculateTurnBasedDelay(parentHeader);

        return new BlockCreationTimeResult(
                result.getTimestampForHeader(), Math.max(0, milliSecondsUntilNextBlock));
    }

    private int calculateTurnBasedDelay(final BlockHeader parentHeader) {
        final RepuProposerSelector proposerSelector = new RepuProposerSelector();
        final Address nextProposer = proposerSelector.selectProposerForNextBlock(parentHeader);

        if (nextProposer.equals(localNodeAddress)) {
            return 0;
        }
        return 1;
    }
}
