//SPDX-License-Identifier: MIT

pragma solidity ^0.8.1;

import "@openzeppelin/contracts/utils/Address.sol";

//Repu consensus contract

contract RepuContract {

    mapping(address => uint256) private validators_reputation;
    address[] private validators;

    mapping(address => uint256) private candidates_votes;
    address[] private candidates;
    address[] private voters;
    address finishVotingValidator;

    mapping(address => uint256) private nodes_nonces;
    mapping(address => uint256) private nodes_blocks;
    address[] private blackList;

    uint256 private index;
    address private proxyAddress;
    Proxy private proxy;
    uint256 private votingRound;
    uint256 private weightBalance = 1;
    uint256 private weightNonce = 3;
    uint256 private weightBlocks = 2;
    uint256 private maxValidators = 5;
    address private owner;

    constructor(address _proxy, address _initValidator, uint256 _votingRound) {
        addValidator(_initValidator);
        updateReputation();
        index = 0;
        proxyAddress = _proxy;
        owner = msg.sender;
        votingRound = _votingRound;
    }

    //modifiers

    modifier isOwner {
        require(msg.sender == owner);
        _;
    }

    modifier isAllowed {
        require(isValidator(msg.sender) || msg.sender == finishVotingValidator);
        _;
    }

    modifier hasCorrectProxyAddress(address _new) {
        address new_proxy = RepuContract(_new).getProxyAddress();
        require(proxyAddress == new_proxy, "Contract proxy address is not correct");
        _;
    }

    modifier isContract(address _new) {
        require(Address.isContract(_new), "Address is not a contract");
        _;
    }

    modifier notVotedYet {
        require(findAddress(msg.sender, voters) == voters.length, "Vote already registered");
        _;
    }

    modifier notVoteHimself(address _addr){
        require(msg.sender != _addr, "A node can not vote himself");
        _;
    }

    modifier isVotingRound {
        require((getBlock() + 1) % votingRound == 0 || getBlock() % votingRound == 0, "Not in votation time");
        _;
    }

    modifier notInBlackList(address _addr) {
        require(findAddress(_addr, blackList) == blackList.length, "Address in the black list");
        require(findAddress(msg.sender, blackList) == blackList.length, "Voter in the black list");
        _;
    }

    //validator methods

    function getSortedValidators() private view returns (address[] memory) {
        uint256[] memory reputations = new uint256[](validators.length);
        address[] memory sortedValidators = validators;

        for (uint256 i = 0; i < validators.length; i++) {
            reputations[i] = validators_reputation[validators[i]];
        }

        bubbleSort(reputations, sortedValidators);
        return sortedValidators;
    }

    function bubbleSort(uint256[] memory arr1, address[] memory arr2) private pure {
        uint len = arr1.length;
        uint256 temp1;
        address temp2;
        for (uint i = 0; i < len; i++) {
            for (uint j = 1; j < (len - i); j++) {
                if (arr1[j-1] < arr1[j]) {
                    temp1 = arr1[j-1];
                    temp2 = arr2[j-1];
                    arr1[j-1] = arr1[j];
                    arr2[j-1] = arr2[j];
                    arr1[j] = temp1;
                    arr2[j] = temp2;
                }
            }
        }
    }

    function nextValidators() public view returns (address[] memory) {
        uint256 i = index % validators.length;
        uint256 j = 0;
        address[] memory list = new address[](validators.length);
        while (i < validators.length) {
            list[j] = validators[i];
            i++;
            j++;
        }
        i = 0;
        while (i < index % validators.length) {
            list[j] = validators[i];
            i++;
            j++;
        }
        return list;
    }

    function addValidator(address _addr) private {
        validators.push(_addr);
        validators_reputation[_addr] = 0;
    }

    function addValidators(address[] memory _addresses) private {
        uint256 acceptedValidators = 0;
        for (uint i = 0; i < _addresses.length && acceptedValidators < maxValidators; i++) {
            //has voted, has enough balance and not in the black list
            if (nodes_nonces[_addresses[i]] > 0
            && findAddress(_addresses[i], blackList) == blackList.length
                && _addresses[i].balance > 0) {
                if (isValidator(_addresses[i])) {
                    deleteFromValidators(_addresses[i]);
                }
                else if (validators.length + i == maxValidators) {
                    validators.pop();
                }
                acceptedValidators++;
            }
        }

        for (uint i = 0; i < _addresses.length && i < maxValidators; i++) {
            if (nodes_nonces[_addresses[i]] > 0
            && findAddress(_addresses[i], blackList) == blackList.length
                && _addresses[i].balance > 0) {
                addValidator(_addresses[i]);
                candidates_votes[_addresses[i]] = 0;
            }
        }
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function isValidator(address _addr) public view returns (bool){
        return findAddress(_addr, validators) != validators.length;
    }

    function nextTurn() isAllowed public {
        uint256 i = index % validators.length;
        nodes_blocks[validators[i]]++;
        index++;
        if ((getBlock() - 1) % votingRound == 0) {
            finishVoting();
            index = 0;
        }
    }

    function nextTurnAndVote(address _addr, uint256 _nonce) isAllowed public {
        nextTurn();
        if (getBlock() % votingRound == 0) {
            voteValidator(_addr, _nonce);
        }
    }

    function deleteFromValidators(address _addr) private {
        uint256 i = findAddress(_addr, validators);
        require(i < validators.length, "Validator not found.");

        validators[i] = validators[validators.length - 1];

        validators.pop();
        delete validators_reputation[_addr];
    }

    function deleteValidator(address _addr) isOwner public {
        deleteFromValidators(_addr);
        updateReputation();
    }

    function findAddress(address _addr, address[] memory list) private pure returns (uint256) {
        for (uint i = 0; i < list.length; i++) {
            if (list[i] == _addr) {
                return i;
            }
        }
        return list.length;
    }

    function getBlackList() public view returns (address[] memory) {
        return blackList;
    }

    function addToBlackList(address _addr) private {
        blackList.push(_addr);
        if (isValidator(_addr)) {
            deleteFromValidators(_addr);
        }
    }

    function getBlock() public view returns (uint256) {
        return block.number;
    }

    //reputation methods

    function updateReputation() private {
        for (uint256 i = 0; i < validators.length; i++) {
            validators_reputation[validators[i]] = calculateReputation(validators[i]);
        }
        validators = getSortedValidators();
    }

    function calculateReputation(address _addr) private view returns (uint256) {
        return
        ((_addr.balance / (10 ** 18)) * weightBalance)
        + (nodes_nonces[_addr] * weightNonce)
        + (nodes_blocks[_addr] * weightBlocks);
    }

    function setWeightBalance(uint256 _newWeightBalance) isOwner external {
        weightBalance = _newWeightBalance;
    }

    function setWeightNonce(uint256 _newWeightNonce) isOwner external {
        weightNonce = _newWeightNonce;
    }

    function setWeightBlocks(uint256 _newWeightBlocks) isOwner external {
        weightBlocks = _newWeightBlocks;
    }

    function setMaxValidators(uint256 _newMaxValidators) isOwner external {
        maxValidators = _newMaxValidators;
    }

    function getBalance(address _addr) public view returns(uint256) {
        return _addr.balance / (10 ** 18);
    }

    function getProducedBlocks(address _addr) public view returns(uint256) {
        return nodes_blocks[_addr];
    }

    function getReputation(address _addr) public view returns(uint256) {
        return calculateReputation(_addr);
    }

    //votation methods

    function voteValidator(address _addr, uint256 nonce) isVotingRound notVotedYet notVoteHimself(_addr) notInBlackList(_addr) public {
        if (nodes_nonces[msg.sender] >= nonce) {
            addToBlackList(_addr);
        } else {
            nodes_nonces[msg.sender] = nonce;

            voters.push(msg.sender);
            if (candidates_votes[_addr] == 0) {
                candidates.push(_addr);
            }
            candidates_votes[_addr] += calculateReputation(msg.sender);
            candidates = getSortedCandidates();
        }
    }

    function finishVoting() private {
        finishVotingValidator = msg.sender;
        delete voters;

        addValidators(candidates);
        for (uint i = 0; i < candidates.length; i++) {
            candidates_votes[candidates[i]] = 0;
        }
        delete candidates;

        updateReputation();
    }

    function getSortedCandidates() public view returns (address[] memory) {
        uint256[] memory votes = new uint256[](candidates.length);
        address[] memory sortedCandidates = new address[](candidates.length);

        for (uint256 i = 0; i < candidates.length; i++) {
            votes[i] = candidates_votes[candidates[i]];
            sortedCandidates[i] = candidates[i];
        }

        bubbleSort(votes, sortedCandidates);
        return sortedCandidates;
    }

    function getVoters() public view returns (address[] memory) {
        return voters;
    }

    function getCandidates() public view returns (address[] memory) {
        return candidates;
    }

    function getVotes(address _addr) public view returns(uint256) {
        return candidates_votes[_addr];
    }

    //proxy methods

    function getProxyAddress() public view returns (address) {
        return (proxyAddress);
    }

    function updateContractAddress(address _new) hasCorrectProxyAddress(_new) isContract(_new) isOwner external {
        Proxy(proxyAddress).setConsensusAddress(_new);
    }
}

contract Proxy {
    function getConsensusAddress() public view returns (address){}

    function setConsensusAddress(address _addr) public {}
}