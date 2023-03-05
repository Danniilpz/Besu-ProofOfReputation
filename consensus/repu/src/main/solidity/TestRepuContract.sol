//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract TestRepuContract{
    address[] validators;
    uint8 index;

    constructor() {
        validators.push(0x1c21335D5E5D3F675D7eB7e19E943535555Bb291);
        validators.push(0x2eD64d60E50f820B240EB5905B0a73848B2506d6);
        index = 0;
    }

    function nextValidator() public view returns (address) {
        return validators[index % validators.length];
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function addValidators(address _addr) public {
        validators.push(_addr);
    }

    function updateValidators() public {
        index++;
    }

    //function deleteValidator(address _addr) public {
    //    validator = _addr;
    //}
}