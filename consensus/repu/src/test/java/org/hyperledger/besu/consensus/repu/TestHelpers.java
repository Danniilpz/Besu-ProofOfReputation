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

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECPSignature;
import org.hyperledger.besu.crypto.SignatureAlgorithmFactory;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderTestFixture;

import java.util.List;

public class TestHelpers {

  public static BlockHeader createRepuSignedBlockHeader(
      final BlockHeaderTestFixture blockHeaderBuilder,
      final KeyPair signer,
      final List<Address> validators) {

    final BlockHeader unsealedHeader =
        blockHeaderBuilder
            .blockHeaderFunctions(new RepuBlockHeaderFunctions())
            .extraData(RepuExtraData.encodeUnsealed(Bytes.wrap(new byte[32]), validators))
            .buildHeader();
    final RepuExtraData unsignedExtraData = RepuExtraData.decodeRaw(unsealedHeader);

    final Hash signingHash =
        RepuBlockHashing.calculateDataHashForProposerSeal(unsealedHeader, unsignedExtraData);

    final SECPSignature proposerSignature =
        SignatureAlgorithmFactory.getInstance().sign(signingHash, signer);

    final Bytes signedExtraData =
        new RepuExtraData(
                unsignedExtraData.getVanityData(),
                proposerSignature,
                unsignedExtraData.getValidators(),
                unsealedHeader)
            .encode();

    blockHeaderBuilder.extraData(signedExtraData);

    return blockHeaderBuilder.buildHeader();
  }
}
