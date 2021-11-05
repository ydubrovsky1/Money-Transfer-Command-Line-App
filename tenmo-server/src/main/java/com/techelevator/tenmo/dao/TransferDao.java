package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFunds;
import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    //find by transfer id
    Transfer getTransferById(int transferId) throws UserDoesNotExist;

    //list all transfers
    List<Transfer> getAllUserTransfers(int accountId) throws UserDoesNotExist;

    //transfer funds
    Transfer transferFunds(Transfer transferFromClient) throws UserDoesNotExist, InsufficientFunds;

}
