package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getBalance(int accountId) throws UserDoesNotExist{
        //FIXME changed to use account ID instead of user ID
        String sql = "SELECT balance " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        Double results = jdbcTemplate.queryForObject(sql, Double.class, accountId);
        if(results != null) {
            return results;
        } else {
            throw new UserDoesNotExist();
        }
    }

    @Override
    public void withdraw(Transfer transferFromClient) {
        String sql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferFromClient.getAccount_from(), transferFromClient.getAmount());
    }

    @Override
    public void deposit(Transfer transferFromClient) {
        String sql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferFromClient.getAccount_from(), transferFromClient.getAmount());
    }

    @Override
    public Account getAccountByUserName(String userName) throws UserDoesNotExist {
        Account returnAccount = null;
        String sql = "SELECT a.account_id, a.user_id, a.balance, u.username  " +
                "FROM accounts a " +
                "JOIN users u " +
                "ON u.user_id = a.user_id " +
                "WHERE u.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userName);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            throw new UserDoesNotExist();
        }
    }

    @Override
    public Account getAccountByAccountId(int accountId) throws UserDoesNotExist {
        Account returnAccount = null;
        String sql = "SELECT a.account_id, a.user_id, a.balance, u.username  " +
                "FROM accounts a " +
                "JOIN users u " +
                "ON u.user_id = a.user_id " +
                "WHERE a.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            throw new UserDoesNotExist();
        }
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setAccount_id(result.getInt("account_id"));
        account.setUser_id(result.getInt("user_id"));
        account.setBalance(result.getDouble("balance"));
        account.setUsername(result.getString("username"));
        return account;
    }
}
