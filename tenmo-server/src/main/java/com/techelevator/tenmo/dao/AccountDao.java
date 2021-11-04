package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface AccountDao {

    //get account balance
    double getBalance(int accountId) throws UserDoesNotExist;

    //withdraw from account balance
    //FIXME changed to take a transfer and use account_to and from as amount
    void withdraw(Transfer transferFromClient);

    //deposit
    void deposit(Transfer transferFromClient);

    //gets accountId using userId
    Account getAccountByUserName(String userName) throws UserDoesNotExist;



}
