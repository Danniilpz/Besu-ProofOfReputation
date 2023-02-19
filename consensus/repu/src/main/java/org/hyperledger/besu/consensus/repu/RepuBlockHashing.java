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
import org.hyperledger.besu.crypto.SECPSignature;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.Util;
import org.hyperledger.besu.ethereum.rlp.BytesValueRLPOutput;

import java.util.function.Supplier;

public class RepuBlockHashing {
  /**
   * Constructs a hash of the block header, suitable for use when creating the proposer seal. The
   * extra data is modified to have a null proposer seal and empty list of committed seals.
   *
   * @param header The header for which a proposer seal is to be calculated
   * @param repuExtraData The extra data block which is to be inserted to the header once seal is
   *     calculated
   * @return the hash of the header suitable for signing as the proposer seal
   */
  public static Hash calculateDataHashForProposerSeal(
      final BlockHeader header, final RepuExtraData repuExtraData) {
    final Bytes headerRlp = serializeHeaderWithoutProposerSeal(header, repuExtraData);
    return Hash.hash(headerRlp); // Proposer hash is the hash of the RLP
  }

  /**
   * Recovers the proposer's {@link Address} from the proposer seal.
   *
   * @param header the block header that was signed by the proposer seal
   * @param repuExtraData the parsed RepuExtraData from the header
   * @return the proposer address
   */
  public static Address recoverProposerAddress(
      final BlockHeader header, final RepuExtraData repuExtraData) {
    if (!repuExtraData.getProposerSeal().isPresent()) {
      if (header.getNumber() == BlockHeader.GENESIS_BLOCK_NUMBER) {
        return Address.ZERO;
      }
      throw new IllegalArgumentException(
          "Supplied repuExtraData does not include a proposer " + "seal");
    }
    final Hash proposerHash = calculateDataHashForProposerSeal(header, repuExtraData);
    return Util.signatureToAddress(repuExtraData.getProposerSeal().get(), proposerHash);
  }

  private static Bytes serializeHeaderWithoutProposerSeal(
      final BlockHeader header, final RepuExtraData repuExtraData) {
    return serializeHeader(header, () -> encodeExtraDataWithoutProposerSeal(repuExtraData));
  }

  private static Bytes encodeExtraDataWithoutProposerSeal(final RepuExtraData repuExtraData) {
    final Bytes extraDataBytes = repuExtraData.encode();
    // Always trim off final 65 bytes (which maybe zeros)
    return extraDataBytes.slice(0, extraDataBytes.size() - SECPSignature.BYTES_REQUIRED);
  }

  private static Bytes serializeHeader(
      final BlockHeader header, final Supplier<Bytes> extraDataSerializer) {
    final BytesValueRLPOutput out = new BytesValueRLPOutput();
    out.startList();

    out.writeBytes(header.getParentHash());
    out.writeBytes(header.getOmmersHash());
    out.writeBytes(header.getCoinbase());
    out.writeBytes(header.getStateRoot());
    out.writeBytes(header.getTransactionsRoot());
    out.writeBytes(header.getReceiptsRoot());
    out.writeBytes(header.getLogsBloom());
    out.writeBytes(header.getDifficulty().toMinimalBytes());
    out.writeLongScalar(header.getNumber());
    out.writeLongScalar(header.getGasLimit());
    out.writeLongScalar(header.getGasUsed());
    out.writeLongScalar(header.getTimestamp());
    out.writeBytes(extraDataSerializer.get());
    out.writeBytes(header.getMixHash());
    out.writeLong(header.getNonce());
    header.getBaseFee().ifPresent(out::writeUInt256Scalar);
    out.endList();
    return out.encoded();
  }
}
