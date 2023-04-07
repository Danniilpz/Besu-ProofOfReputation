//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple consensus contract test

contract TestRepuContract {
    address[] validators;
    uint256 index;
    address public proxy;
    uint256 lastBlock;

    constructor(address _proxy) {
        validators.push(0x1c21335D5E5D3F675D7eB7e19E943535555Bb291);
        validators.push(0x2eD64d60E50f820B240EB5905B0a73848B2506d6);
        validators.push(0x11F8EBFF1B0fFb4dE7814Cc25430D01149fcDC71);
        index = 0;
        proxy = _proxy;
        lastBlock = 0;
    }

    function nextValidators() public view returns (address[] memory) {
        uint256 i = index % validators.length;
        uint256 j = 0;
        address[] memory list = new address[](validators.length);
        while(i < validators.length){
            list[j] = validators[i];
            i++;
            j++;
        }
        i = 0;
        while(i < index % validators.length){
            list[j] = validators[i];
            i++;
            j++;
        }
        return list;
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function getLastBlock() public view returns (uint256){
        return lastBlock;
    }

    function addValidator(address _addr) public {
        validators.push(_addr);
    }

    function getProxyAddress() public view returns (address, bool) {
        return (proxy, true);
    }

    function updateValidators(uint256 _lastBlock) public {
        index++;
        lastBlock = _lastBlock;
    }

    //function deleteValidator(address _addr) public {
    //    validator = _addr;
    //}

    modifier hasCorrectProxyAddress(address _new){
        (bool success,bytes memory data) = _new.call(abi.encodeWithSignature('getProxyAddress()'));
        (address new_proxy, bool b) = abi.decode(data,(address, bool));
        require(proxy == new_proxy, "Proxy address is not correct");
        _;
    }

    function updateContractAddress(address _new) hasCorrectProxyAddress(_new) external{
        (bool success, ) = proxy.call(abi.encodeWithSignature('setConsensusAddress(address)', _new));
        require(success,"Error");
    }
}