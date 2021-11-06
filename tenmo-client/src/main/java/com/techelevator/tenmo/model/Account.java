package com.techelevator.tenmo.model;

public class Account {
    private int account_id;
    private int user_id;
    private double balance;
    private String username;

    public Account(){}

    public Account(int account_id, int user_id, double balance, String username) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.balance = balance;
        this.username = username;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public double getBalance() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Account id: " + this.account_id +
                "\nuser id: " + this.user_id +
                "\nbalance: " + this.balance +
                "\nusername: " + this.username;
    }
}
