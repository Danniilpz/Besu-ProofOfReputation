// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Proxy {
    address private consensusContract;

    modifier isAllowed{
        require(consensusContract == 0x0000000000000000000000000000000000000000 || msg.sender == consensusContract, "You are not autorized");
        _;
    }

    function getConsensusAddress() public view returns (address) {
        return consensusContract;
    }

    function setConsensusAddress(address _addr) public isAllowed{
        consensusContract = _addr;
    }
}
