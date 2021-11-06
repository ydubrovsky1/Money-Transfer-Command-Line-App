package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFunds;
import com.techelevator.tenmo.exception.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao, AccountDao accountdao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.accountDao = accountdao;
    }

    @Override
    public Transfer getTransferById(int transferId) throws UserDoesNotExist{
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, tt.transfer_type_desc, ts.transfer_status_desc " +
                "FROM transfers t " +
                "JOIN transfer_types tt " +
                "ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_statuses ts " +
                "ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if(rowSet.next()){
            return mapRowtoTransfer(rowSet);
        }
        return null;
    }

    @Override
    public List<Transfer> getAllUserTransfers(int accountId) throws UserDoesNotExist {
        List<Transfer> result = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, tt.transfer_type_desc, ts.transfer_status_desc " +
                "FROM transfers t " +
                "JOIN accounts a " +
                "ON t.account_from = a.account_id " +
                "JOIN transfer_types tt " +
                "ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_statuses ts " +
                "ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE ? = account_from OR ? = account_to;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rowSet.next()) {
            result.add(mapRowtoTransfer(rowSet));
        }
        return result;
    }

//


    private Transfer mapRowtoTransfer(SqlRowSet rs) throws UserDoesNotExist{
        Transfer transfer = new Transfer();
        Account account_from = accountDao.getAccountByAccountId(rs.getInt("account_from"));
        Account account_to = accountDao.getAccountByAccountId(rs.getInt("account_to"));

        transfer.setAccount_to(account_to);
        transfer.setAccount_from(account_from);
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_type_desc(rs.getString("transfer_type_desc"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setTransfer_status_desc(rs.getString("transfer_status_desc"));
        transfer.setAccount_from_id(rs.getInt("account_from"));
        transfer.setAccount_to_id(rs.getInt("account_to"));
        transfer.setAmount(rs.getDouble("amount"));

        return transfer;
    }

    @Override
    public Transfer transferFunds(Transfer transferFromClient) throws UserDoesNotExist, InsufficientFunds {
        //intake userid, recipientid, and amount
        // check recipient exists - error if not
        Transfer returnTransfer = null;
        if(transferFromClient.getAccount_to_id() > 0) {
            //check amount is less than or equal to user balance - error if not
            if(accountDao.getBalance(transferFromClient.getAccount_from_id()) >= transferFromClient.getAmount()) {//accountDao.getBalance(userId) >= transferAmt
                //decrease user by amount
                accountDao.withdraw(transferFromClient);
                //increase recipient by amount
                accountDao.deposit(transferFromClient);
                String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (?,?,?,?,?) RETURNING transfer_id;";
                Integer newId = jdbcTemplate.queryForObject(
                        sql, Integer.class, 2, 2, transferFromClient.getAccount_from_id(), transferFromClient.getAccount_to_id(), transferFromClient.getAmount()
                );
                returnTransfer = getTransferById(newId);
            } else {
                throw new InsufficientFunds();
            }
        } else {
            throw new UserDoesNotExist();
        }
        //return success and new balance - return transfer or ?
        return returnTransfer;
    }

}



//Fixed this method to new one
//@Override
//    public Transfer transferFunds(int userId, String recipientName, double transferAmt) throws UserDoesNotExist, InsufficientFunds {
//        //intake userid, recipientid, and amount
//        // check recipient exists - error if not
//        Transfer returnTransfer = null;
//            if(userDao.findIdByUsername(recipientName) > 0) {
//                //check amount is less than or equal to user balance - error if not
//                if(accountDao.getBalance(userId) >= transferAmt) {
//                    int userAccountID = accountDao.getAccountByUserId(userId);
//                    int recipientAccountId = accountDao.getAccountByUserId(userDao.findIdByUsername(recipientName));
//                    //decrease user by amount
//                    accountDao.withdraw(userAccountID, transferAmt);
//                    //increase recipient by amount
//                    accountDao.deposit(recipientAccountId, transferAmt);
//                    String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                            "VALUES (?,?,?,?,?) RETURNING transfer_id;";
//                    Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, 2, 2, userAccountID, recipientAccountId, transferAmt);
//                    returnTransfer = getTransferById(newId);
//                } else {
//                    throw new InsufficientFunds();
//                }
//            } else {
//                throw new UserDoesNotExist();
//            }
//            //return success and new balance - return transfer or ?
//        return returnTransfer;
//    }