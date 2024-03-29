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

import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class RepuRequestFactory {

  public static class ProposeResponse extends Response<Boolean> {}

  public static class DiscardResponse extends Response<Boolean> {}

  public static class SignersBlockResponse extends Response<List<Address>> {}

  public static class ProposalsResponse extends Response<Map<Address, Boolean>> {}

  private final Web3jService web3jService;

  public RepuRequestFactory(final Web3jService web3jService) {
    this.web3jService = web3jService;
  }

  Request<?, ProposeResponse> repuPropose(final String address, final Boolean auth) {
    return new Request<>(
        "repu_propose",
        Arrays.asList(address, auth.toString()),
        web3jService,
        ProposeResponse.class);
  }

  Request<?, DiscardResponse> repuDiscard(final String address) {
    return new Request<>(
        "repu_discard", singletonList(address), web3jService, DiscardResponse.class);
  }

  Request<?, ProposalsResponse> repuProposals() {
    return new Request<>("repu_proposals", emptyList(), web3jService, ProposalsResponse.class);
  }

  Request<?, SignersBlockResponse> repuGetSigners(final String blockNumber) {
    return new Request<>(
        "repu_getSigners", singletonList(blockNumber), web3jService, SignersBlockResponse.class);
  }

  Request<?, SignersBlockResponse> repuGetSignersAtHash(final Hash hash) {
    return new Request<>(
        "repu_getSignersAtHash",
        singletonList(hash.toString()),
        web3jService,
        SignersBlockResponse.class);
  }
}
