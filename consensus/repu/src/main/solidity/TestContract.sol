//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Simple counter

contract Counter{
    uint256 count;
    constructor(uint256 _count) {
        count=_count; 
    }
    function setCount(uint256 _count) public {
        count=_count;
    }
    function incrementCount() public {
        count+=1;
    }
    function getCount() public view returns(uint256) {
        return count;
    }
    function getNumber() public pure returns(uint256) {
        return 34;
    }
}