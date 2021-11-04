package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserDoesNotExist;
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
    public double getBalance(int userId) throws UserDoesNotExist{
        String sql = "SELECT a.balance " +
                "FROM accounts a " +
                "JOIN users u " +
                "ON u.user_id = a.user_id " +
                "WHERE a.user_id = ?;";
        Double results = jdbcTemplate.queryForObject(sql, Double.class, userId);
        if(results != null) {
            return results;
        } else {
            throw new UserDoesNotExist();
        }
    }

    @Override
    public void withdraw(int accountId, double amount) {
        String sql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public void deposit(int accountId, double amount) {
        String sql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public int getAccountByUserId(int userId) throws UserDoesNotExist {
        String sql = "SELECT a.account_id  " +
                "FROM accounts a " +
                "JOIN users u " +
                "ON u.user_id = a.user_id " +
                "WHERE a.user_id = ?;";
        Integer results = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (results != null) {
            return results;
        } else {
            throw new UserDoesNotExist();
        }
    }
}
