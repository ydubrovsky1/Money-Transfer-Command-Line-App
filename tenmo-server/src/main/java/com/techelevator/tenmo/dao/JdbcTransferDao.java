package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(int transferId) {
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
    public List<Transfer> getAllUserTransfers(int userId) {
        return null;
    }

    @Override
    public Transfer transferFunds(int userId, int recipientId, double transferAmt) {
        return null;
    }

    private Transfer mapRowtoTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_type_desc(rs.getString("transfer_type_desc"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setTransfer_status_desc(rs.getString("transfer_status_desc"));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));
        transfer.setAmount(rs.getDouble("amount"));

        return transfer;
    }


}
