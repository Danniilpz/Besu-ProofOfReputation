//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract TestRepuContract {
    address[] validators;
    uint256 currentBlock;

    //atributo address proxy
    constructor(uint256 _current) {
        validators.push(0x1c21335D5E5D3F675D7eB7e19E943535555Bb291);
        validators.push(0x2eD64d60E50f820B240EB5905B0a73848B2506d6);
        validators.push(0x11F8EBFF1B0fFb4dE7814Cc25430D01149fcDC71);
        currentBlock = _current;
    }

    function nextValidator() public view returns (address) {
        return validators[currentBlock % validators.length];
    }

    function nextBlock() public view returns (uint256) {
        return currentBlock;
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function addValidators(address _addr) public {
        validators.push(_addr);
    }

    function updateValidators() public {
        currentBlock++; //recibir numero de blockque como parametro
    }

    //function deleteValidator(address _addr) public {
    //    validator = _addr;
    //}
}