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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.assertj.core.util.Lists;
import org.hyperledger.besu.consensus.common.validator.ValidatorProvider;
import org.hyperledger.besu.consensus.repu.RepuBlockInterface;
import org.hyperledger.besu.consensus.repu.RepuContext;
import org.hyperledger.besu.consensus.repu.RepuMiningTracker;
import org.hyperledger.besu.consensus.repu.TestHelpers;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SignatureAlgorithm;
import org.hyperledger.besu.crypto.SignatureAlgorithmFactory;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.ethereum.ProtocolContext;
import org.hyperledger.besu.ethereum.chain.MutableBlockchain;
import org.hyperledger.besu.ethereum.core.*;
import org.hyperledger.besu.ethereum.eth.sync.state.SyncState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperledger.besu.ethereum.core.InMemoryKeyValueStorageProvider.createInMemoryBlockchain;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RepuMiningCoordinatorTest {

  private static final Supplier<SignatureAlgorithm> SIGNATURE_ALGORITHM =
      Suppliers.memoize(SignatureAlgorithmFactory::getInstance);

  private final KeyPair proposerKeys = SIGNATURE_ALGORITHM.get().generateKeyPair();
  private final KeyPair validatorKeys = SIGNATURE_ALGORITHM.get().generateKeyPair();
  private final Address proposerAddress = Util.publicKeyToAddress(proposerKeys.getPublicKey());
  private final Address validatorAddress = Util.publicKeyToAddress(validatorKeys.getPublicKey());

  private final List<Address> validators = Lists.newArrayList(validatorAddress, proposerAddress);

  private final BlockHeaderTestFixture headerTestFixture = new BlockHeaderTestFixture();

  private RepuMiningTracker miningTracker;
  private final RepuBlockInterface blockInterface = new RepuBlockInterface();

  @Mock private MutableBlockchain blockChain;
  @Mock private ProtocolContext protocolContext;
  @Mock private RepuMinerExecutor minerExecutor;
  @Mock private RepuBlockMiner blockMiner;
  @Mock private SyncState syncState;
  @Mock private ValidatorProvider validatorProvider;

  @Before
  public void setup() {

    headerTestFixture.number(1);
    Block genesisBlock = createEmptyBlock(0, Hash.ZERO, proposerKeys); // not normally signed but ok
    blockChain = createInMemoryBlockchain(genesisBlock);

    when(validatorProvider.getValidatorsAfterBlock(any())).thenReturn(validators);
    final RepuContext repuContext = new RepuContext(validatorProvider, null, blockInterface);

    when(protocolContext.getConsensusContext(RepuContext.class)).thenReturn(repuContext);
    when(protocolContext.getBlockchain()).thenReturn(blockChain);
    when(minerExecutor.startAsyncMining(any(), any(), any())).thenReturn(Optional.of(blockMiner));
    when(syncState.isInSync()).thenReturn(true);

    miningTracker = new RepuMiningTracker(proposerAddress, protocolContext);
  }

  @Test
  public void outOfTurnBlockImportedDoesNotInterruptInTurnMiningOperation() {
    // As the head of the blockChain is 0 (which effectively doesn't have a signer, all validators
    // are able to propose.

    when(blockMiner.getParentHeader()).thenReturn(blockChain.getChainHeadHeader());

    // Note also - validators is an hard-ordered LIST, thus in-turn will follow said list - block_1
    // should be created by proposer.
    final RepuMiningCoordinator coordinator =
        new RepuMiningCoordinator(blockChain, minerExecutor, syncState, miningTracker);

    coordinator.enable();
    coordinator.start();

    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), any());

    reset(minerExecutor);

    final Block importedBlock = createEmptyBlock(1, blockChain.getChainHeadHash(), validatorKeys);

    blockChain.appendBlock(importedBlock, Collections.emptyList());

    // The minerExecutor should not be invoked as the mining operation was conducted by an in-turn
    // validator, and the created block came from an out-turn validator.
    verify(minerExecutor, never()).startAsyncMining(any(), any(), any());
  }

  @Test
  public void outOfTurnBlockImportedAtHigherLevelInterruptsMiningOperation() {
    // As the head of the blockChain is 1 (which effectively doesn't have a signer, all validators
    // are able to propose.
    when(blockMiner.getParentHeader()).thenReturn(blockChain.getChainHeadHeader());

    // Note also - validators is an hard-ordered LIST, thus in-turn will follow said list - block_1
    // should be created by proposer.
    final RepuMiningCoordinator coordinator =
        new RepuMiningCoordinator(blockChain, minerExecutor, syncState, miningTracker);

    coordinator.enable();
    coordinator.start();

    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), any());

    reset(minerExecutor);
    when(minerExecutor.startAsyncMining(any(), any(), any())).thenReturn(Optional.of(blockMiner));

    final Block importedBlock = createEmptyBlock(2, blockChain.getChainHeadHash(), validatorKeys);

    blockChain.appendBlock(importedBlock, Collections.emptyList());

    // The minerExecutor should not be invoked as the mining operation was conducted by an in-turn
    // validator, and the created block came from an out-turn validator.
    ArgumentCaptor<BlockHeader> varArgs = ArgumentCaptor.forClass(BlockHeader.class);
    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), varArgs.capture());
    assertThat(varArgs.getValue()).isEqualTo(blockChain.getChainHeadHeader());
  }

  @Test
  public void outOfTurnBlockImportedInterruptsOutOfTurnMiningOperation() {
    blockChain.appendBlock(
        createEmptyBlock(1, blockChain.getChainHeadHash(), validatorKeys), Collections.emptyList());

    when(blockMiner.getParentHeader()).thenReturn(blockChain.getChainHeadHeader());

    // Note also - validators is an hard-ordered LIST, thus in-turn will follow said list - block_2
    // should be created by 'validator', thus Proposer is out-of-turn.
    final RepuMiningCoordinator coordinator =
        new RepuMiningCoordinator(blockChain, minerExecutor, syncState, miningTracker);

    coordinator.enable();
    coordinator.start();

    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), any());

    reset(minerExecutor);
    when(minerExecutor.startAsyncMining(any(), any(), any())).thenReturn(Optional.of(blockMiner));

    final Block importedBlock = createEmptyBlock(2, blockChain.getChainHeadHash(), validatorKeys);

    blockChain.appendBlock(importedBlock, Collections.emptyList());

    // The minerExecutor should not be invoked as the mining operation was conducted by an in-turn
    // validator, and the created block came from an out-turn validator.
    ArgumentCaptor<BlockHeader> varArgs = ArgumentCaptor.forClass(BlockHeader.class);
    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), varArgs.capture());
    assertThat(varArgs.getValue()).isEqualTo(blockChain.getChainHeadHeader());
  }

  @Test
  public void outOfTurnBlockImportedInterruptsNonRunningMiner() {
    blockChain.appendBlock(
        createEmptyBlock(1, blockChain.getChainHeadHash(), proposerKeys), Collections.emptyList());

    when(blockMiner.getParentHeader()).thenReturn(blockChain.getChainHeadHeader());

    // Note also - validators is an hard-ordered LIST, thus in-turn will follow said list - block_2
    // should be created by 'validator', thus Proposer is out-of-turn.
    final RepuMiningCoordinator coordinator =
        new RepuMiningCoordinator(blockChain, minerExecutor, syncState, miningTracker);

    coordinator.enable();
    coordinator.start();

    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), any());

    reset(minerExecutor);
    when(minerExecutor.startAsyncMining(any(), any(), any())).thenReturn(Optional.of(blockMiner));

    final Block importedBlock = createEmptyBlock(2, blockChain.getChainHeadHash(), validatorKeys);

    blockChain.appendBlock(importedBlock, Collections.emptyList());

    // The minerExecutor should not be invoked as the mining operation was conducted by an in-turn
    // validator, and the created block came from an out-turn validator.
    ArgumentCaptor<BlockHeader> varArgs = ArgumentCaptor.forClass(BlockHeader.class);
    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), varArgs.capture());
    assertThat(varArgs.getValue()).isEqualTo(blockChain.getChainHeadHeader());
  }

  @Test
  public void locallyGeneratedBlockInvalidatesMiningEvenIfInTurn() {
    // Note also - validators is an hard-ordered LIST, thus in-turn will follow said list - block_1
    // should be created by Proposer, and thus will be in-turn.
    final RepuMiningCoordinator coordinator =
        new RepuMiningCoordinator(blockChain, minerExecutor, syncState, miningTracker);

    coordinator.enable();
    coordinator.start();

    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), any());

    reset(minerExecutor);
    when(minerExecutor.startAsyncMining(any(), any(), any())).thenReturn(Optional.of(blockMiner));

    final Block importedBlock = createEmptyBlock(1, blockChain.getChainHeadHash(), proposerKeys);
    blockChain.appendBlock(importedBlock, Collections.emptyList());

    // The minerExecutor should not be invoked as the mining operation was conducted by an in-turn
    // validator, and the created block came from an out-turn validator.
    ArgumentCaptor<BlockHeader> varArgs = ArgumentCaptor.forClass(BlockHeader.class);
    verify(minerExecutor, times(1)).startAsyncMining(any(), any(), varArgs.capture());
    assertThat(varArgs.getValue()).isEqualTo(blockChain.getChainHeadHeader());
  }

  private Block createEmptyBlock(
      final long blockNumber, final Hash parentHash, final KeyPair signer) {
    headerTestFixture.number(blockNumber).parentHash(parentHash);
    final BlockHeader header =
        TestHelpers.createRepuSignedBlockHeader(headerTestFixture, signer, validators);
    return new Block(header, new BlockBody(Collections.emptyList(), Collections.emptyList()));
  }
}
