package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFunds;
import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    //find by transfer id
    Transfer getTransferById(int transferId);

    //list all transfers
    List<Transfer> getAllUserTransfers(int accountId);

    //transfer funds
    Transfer transferFunds(int userId, String recipientName, double transferAmt) throws UserDoesNotExist, InsufficientFunds;

}
