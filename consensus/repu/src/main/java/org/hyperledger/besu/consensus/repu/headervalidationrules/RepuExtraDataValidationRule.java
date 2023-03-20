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

import com.google.common.collect.Iterables;
import org.hyperledger.besu.consensus.common.EpochManager;
import org.hyperledger.besu.consensus.repu.RepuExtraData;
import org.hyperledger.besu.consensus.repu.RepuHelpers;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.mainnet.AttachedBlockHeaderValidationRule;
import org.hyperledger.besu.ethereum.rlp.RLPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class RepuExtraDataValidationRule implements AttachedBlockHeaderValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(RepuExtraDataValidationRule.class);

  private final EpochManager epochManager;

  public RepuExtraDataValidationRule(final EpochManager epochManager) {
    this.epochManager = epochManager;
  }

  /**
   * Responsible for determining the validity of the extra data field. Ensures:
   *
   * <ul>
   *   <li>Bytes in the extra data field can be decoded as per Repu specification
   *   <li>Proposer (derived from the proposerSeal) is a member of the validators
   *   <li>Validators are only validated on epoch blocks.
   * </ul>
   *
   * @param header the block header containing the extraData to be validated.
   * @return True if the extraData successfully produces an RepuExtraData object, false otherwise
   */
  @Override
  public boolean validate(
      final BlockHeader header, final BlockHeader parent, final ProtocolContext protocolContext) {
    try {
      final Collection<Address> storedValidators = RepuHelpers.getValidators();

      return extraDataIsValid(storedValidators, header);

    } catch (final RLPException ex) {
      LOG.info(
          "Invalid block header: ExtraData field was unable to be deserialised into an Repu Struct.",
          ex);
      return false;
    } catch (final IllegalArgumentException ex) {
      LOG.info("Invalid block header: Failed to verify extra data", ex);
      return false;
    }
  }

  private boolean extraDataIsValid(
      final Collection<Address> expectedValidators, final BlockHeader header) {

    final RepuExtraData repuExtraData = RepuExtraData.decode(header);

    if (epochManager.isEpochBlock(header.getNumber())) {
      if (!Iterables.elementsEqual(repuExtraData.getValidators(), expectedValidators)) {
        LOG.info(
            "Invalid block header: Incorrect signers. Expected {} but got {}.",
            expectedValidators,
            repuExtraData.getValidators());
        return false;
      }
    } else {
      if (!repuExtraData.getValidators().isEmpty()) {
        LOG.info("Invalid block header: Signer list on non-epoch blocks must be empty.");
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean includeInLightValidation() {
    return false;
  }
}
