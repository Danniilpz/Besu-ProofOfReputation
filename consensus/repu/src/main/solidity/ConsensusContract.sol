// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

interface ConsensusContract {
    //struct Node{Address-reputation}
    //list Nodes (who can vote?)
    //mapping Nodes-votes
    //after voting, update list Nodes, add/delete validators, empty list votes
    //modifier isValidator: address is on the list
    //periodic voting (timer modifier?) (when mapping is empty?)
    //update proxy

    //validator methods
    function nextValidator() external view returns(address[]); //round-robin. returns a list of ordered addresses
    function addValidator(address _addr) external returns (bool success); //validate maximum number
    function deleteValidator(address _addr) external returns (bool success);
    function getValidators() public view returns (address[] memory);
    function isValidator(address _addr) external returns (bool);
    function updateValidators(uint256 _lastBlock) public;

    //reputation methods
    function updateReputation() external; //updates reputations in the list Address-Reputation (list of current validators).
    function calculateReputation(address _addr) external returns (uint256); //dynamic reputation for voting nodes-

    //update validator list methods
    function voteValidator(address _addr) external;

    //proxy methods
    function getProxyAddress() public view returns (address, bool);
    function updateContractAddress(address _new) external; //add modifier hasCorrectProxyAddress

    //events
}
