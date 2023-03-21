//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract TestRepuContract {
    address[] validators;
    uint256 index;
    address public proxy;

    constructor() {
        validators.push(0x1c21335D5E5D3F675D7eB7e19E943535555Bb291);
        validators.push(0x2eD64d60E50f820B240EB5905B0a73848B2506d6);
        validators.push(0x11F8EBFF1B0fFb4dE7814Cc25430D01149fcDC71);
        index = 0;
        proxy = 0x9C406DFc7C68231087cdC4F02C246B65fF1557b8;
    }

    function nextValidator() public view returns (address) {
        return validators[index % validators.length];
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function addValidator(address _addr) public {
        validators.push(_addr);
    }

    function updateValidators() public {
        index++;
    }

    //function deleteValidator(address _addr) public {
    //    validator = _addr;
    //}

    function updateContractAddress(address _new) external{
        (bool success,bytes memory data) = proxy.call(abi.encodeWithSignature('setConsensusAddress(address)', _new));
        require(success,"Error");
    }
}