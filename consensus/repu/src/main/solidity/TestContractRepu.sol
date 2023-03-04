//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract Consensus{
    address validator;

    constructor() {
        validator = 0xdbE8422E428429E59c604A0Ae614629b7794B924;
    }

    function nextValidator() public view returns (address){
        return validator;
    }
    function updateValidator(address _addr) public {
        validator = _addr;
    }
}