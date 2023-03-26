//SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;

//Repu consensus contract

contract RepuContract{

    mapping (address => uint256) public validators_reputation;
    address[] validators;

    mapping (address => uint256) public candidates_votes;
    address[] candidates;
    //mapping (address => address) votes;
    address[] voters;

    uint256 index;
    address public proxy;
    uint256 constant public MAX_VALIDATORS = 5;
    uint256 constant public MAX_VOTES = 3;

    constructor(address _proxy, address _initValidator) {
        addValidator(_initValidator);
        addValidator(0x2eD64d60E50f820B240EB5905B0a73848B2506d6);
        addValidator(0x11F8EBFF1B0fFb4dE7814Cc25430D01149fcDC71);
        updateReputation();
        index = 0;
        proxy = _proxy;
    }

    //modifiers

    modifier hasCorrectProxyAddress(address _new){
        (bool success,bytes memory data) = _new.call(abi.encodeWithSignature('getProxyAddress()'));
        (address new_proxy, bool b) = abi.decode(data,(address, bool));
        require(proxy == new_proxy, "Proxy address is not correct");
        _;
    }

    //validator methods

    function getSortedValidators() internal view returns (address[] memory) {
        uint256[] memory reputations = new uint256[](validators.length);
        address[] memory sortedValidators = new address[](validators.length);

        for (uint256 i = 0; i < validators.length; i++) {
            reputations[i] = validators_reputation[validators[i]];
            sortedValidators[i] = validators[i];
        }

        quickSort(reputations, sortedValidators, 0, (sortedValidators.length - 1));
        return sortedValidators;
    }

    //https://stackoverflow.com/a/64661901
    function quickSort(uint256[] memory reputations, address[] memory sortedValidators, uint left, uint right) internal view {
        uint i = left;
        uint j = right;
        if (i == j) return;
        uint256 pivot = reputations[left + (right - left) / 2];
        while (i <= j) {
            while (reputations[i] > pivot) i++;
            while (pivot > reputations[j]) j--;
            if (i <= j) {
                (reputations[i], reputations[j]) = (reputations[j], reputations[i]);
                (sortedValidators[i], sortedValidators[j]) = (sortedValidators[j], sortedValidators[i]);
                i++;
                j--;
            }
        }
        if (left < j)
            quickSort(reputations, sortedValidators, left, j);
        if (i < right)
            quickSort(reputations, sortedValidators, i, right);
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

    function addValidator(address _addr) private {
        require(validators.length < MAX_VALIDATORS, "No more validators accepted.");
        if(!isValidator(_addr)){
            if(validators.length == MAX_VALIDATORS){
                validators.pop();
            }
            validators.push(_addr);
            validators_reputation[_addr] = 0;
        }
    }

    function getValidators() public view returns (address[] memory) {
        return validators;
    }

    function isValidator(address _addr) public view returns (bool){
        return findAddress(_addr) != validators.length;
    }

    function updateValidators() public {
        index++;
    }

    function deleteValidator(address _addr) public {
        uint256 i = findAddress(_addr);
        require(i < validators.length, "Validator not found.");

        for (; i < validators.length - 1; i++) {
            validators[i] = validators[i+1];
        }

        validators.pop();
        delete validators_reputation[_addr];
        updateReputation();
    }

    function findAddress(address _addr) private view returns (uint256) {
        for (uint i = 0; i < validators.length; i++) {
            if (validators[i] == _addr) {
                return i;
            }
        }
        return validators.length;
    }

    function getBlock() public view returns (uint256) {
        return block.number;
    }

    //reputation methods

    function updateReputation() private{
        for(uint256 i = 0; i < validators.length; i++){
            validators_reputation[validators[i]] = calculateReputation(validators[i]);
        }
        validators = getSortedValidators();
    }

    function calculateReputation(address _addr) private view  returns (uint256){
        return _addr.balance;
    }

    //votation methods

    function voteValidator(address _addr) external{
        //check not duplicated votes
        //ponderar voto
        voters.push(msg.sender);
        //votes[msg.sender] = _addr;
        if(candidates_votes[_addr] == 0) {
            candidates.push(_addr);
        }
        candidates_votes[_addr]++;

        if(voters.length >= MAX_VOTES){
            finishVoting();
        }
    }

    function finishVoting() private {
        address[] memory sortedCandidates = getSortedCandidates();
        delete candidates;
        delete voters;
        for (uint i = 0; i < sortedCandidates.length && i < MAX_VALIDATORS; i++){
            addValidator(sortedCandidates[i]);
        }
        updateReputation();
    }

    function getSortedCandidates() internal view returns (address[] memory) {
        uint256[] memory votes = new uint256[](candidates.length);
        address[] memory sortedCandidates = new address[](candidates.length);

        for (uint256 i = 0; i < candidates.length; i++) {
            votes[i] = candidates_votes[candidates[i]];
            sortedCandidates[i] = candidates[i];
        }

        quickSort(votes, sortedCandidates, 0, (sortedCandidates.length - 1));
        return sortedCandidates;
    }

    function getVoters() public view returns (address[] memory) {
        return voters;
    }

    function getCandidates() public view returns (address[] memory) {
        return candidates;
    }

    //proxy methods

    function getProxyAddress() public view returns (address, bool) {
        return (proxy, true);
    }

    function updateContractAddress(address _new) hasCorrectProxyAddress(_new) external{
        (bool success, ) = proxy.call(abi.encodeWithSignature('setConsensusAddress(address)', _new));
        require(success,"Error");
    }
}