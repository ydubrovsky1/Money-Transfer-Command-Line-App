package com.techelevator.tenmo.model;

public class Transfer {
    private int transfer_id;
    private int transfer_type_id;
    private String transfer_type_desc;
    private int transfer_status_id;
    private String transfer_status_desc;
    //ToDO: Account objects or just ints account numbers?
    private Account account_from;
    private int account_from_id;
    private Account account_to;
    private int account_to_id;
    private double amount;

    public Transfer(){}

    public Transfer(int transfer_id, int transfer_type_id, String transfer_type_desc, int transfer_status_id, String transfer_status_desc, Account account_from, int account_from_id, Account account_to, int account_to_id, double amount){
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_type_desc = transfer_type_desc;
        this.transfer_status_id = transfer_status_id;
        this.transfer_status_desc = transfer_status_desc;
        this.account_from = account_from;
        this.account_from_id = account_from_id;
        this.account_to = account_to;
        this.account_to_id = account_to_id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Id: " + this.transfer_id +
                "\nFrom: " + this.account_from.getUsername() +
                "\nTo: " + this.account_to.getUsername() +
                "\nType: " + this.transfer_type_desc +
                "\nStatus: " + this.transfer_status_desc +
                "\nAmount: " + this.amount;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public void setTransfer_type_desc(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public String getTransfer_status_desc() {
        return transfer_status_desc;
    }

    public void setTransfer_status_desc(String transfer_status_desc) {
        this.transfer_status_desc = transfer_status_desc;
    }

    public Account getAccount_from() {
        return account_from;
    }

    public void setAccount_from(Account account_from) {
        this.account_from = account_from;
    }

    public int getAccount_from_id() {
        return account_from_id;
    }

    public void setAccount_from_id(int account_from_id) {
        this.account_from_id = account_from_id;
    }

    public Account getAccount_to() {
        return account_to;
    }

    public void setAccount_to(Account account_to) {
        this.account_to = account_to;
    }

    public int getAccount_to_id() {
        return account_to_id;
    }

    public void setAccount_to_id(int account_to_id) {
        this.account_to_id = account_to_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}