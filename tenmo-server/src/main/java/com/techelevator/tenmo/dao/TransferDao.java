package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    //find by transfer id
    Transfer getTransferById(int transferId);

    //list all transfers
    List<Transfer> getAllUserTransfers(int userId);

    //transfer funds
    Transfer transferFunds(int userId, int recipientId, double transferAmt);

}