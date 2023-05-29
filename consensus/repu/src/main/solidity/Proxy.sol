// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Proxy {
    address private consensusAddress;

    constructor(address _addr) {
        consensusAddress = _addr;
    }

    modifier isAllowed{
        require(msg.sender == consensusAddress, "You are not authorized");
        _;
    }

    function getConsensusAddress() public view returns (address) {
        return consensusAddress;
    }

    function setConsensusAddress(address _addr) public isAllowed{
        consensusAddress = _addr;
    }
}
