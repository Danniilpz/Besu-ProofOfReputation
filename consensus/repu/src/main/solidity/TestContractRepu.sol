//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract Consensus{
    address validator;

    constructor(address _addr) {
        validator = _addr;
    }

    function nextValidator() public view returns (address){
        return validator;
    }
    function updateValidator(address _addr) public {
        validator = _addr;
    }
}