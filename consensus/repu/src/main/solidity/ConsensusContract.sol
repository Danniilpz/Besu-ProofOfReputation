// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

interface ConsensusContract {
    //struct Node{Address-reputation}
    //list Nodes
    //mapping Nodes-votes
    //max. validators
    //after voting, update list address-repu, add/delete validators, empty list votes
    //modifier isValidator: address is on the list
    //periodic voting (timer modifier?) (when mapping is empty?)
    //timeout (java)
    //update proxy
    function nextValidator() external view returns(address[]); //round-robin. returns a list of ordered addresses
    function addValidator(address _addr) external returns (bool success);
    function deleteValidator(address _addr) external returns (bool success);
    function isValidator(address _addr) external returns (bool);
    function updateReputation() external; //updates reputations in the list Address-Reputation (list of current validators).
    function calculateReputation(address _addr) external returns (uint256); //dynamic reputation for voting nodes-
    function voteValidator(address _addr) external;

    //events
}
