package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserDoesNotExist;

public interface AccountDao {

    //get account balance
    double getBalance(int userId) throws UserDoesNotExist;

    //withdraw from account balance
    void withdraw(int accountId, double amount);

    //deposit
    void deposit(int accountId, double amount);

    //gets accountId using userId
    int getAccountByUserId(int userId) throws UserDoesNotExist;

}
