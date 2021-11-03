package com.techelevator.tenmo.dao;

public interface AccountDao {

    //get account balance
    double getBalance(int userId);

    //withdraw from account balance
    double withdraw(int accountId, double amount);

    //deposit
    double deposit(int accountId, double amount);

}
