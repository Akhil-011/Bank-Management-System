package com.akhil.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.model.Transaction;

public class TransactionRepository {

    /**
     * Save a transaction
     */
    public boolean saveTransaction(Transaction transaction) {

    //System.out.println(">>> Inside saveTransaction()");

    String sql = """
        INSERT INTO transactions
        (account_id, transaction_type, amount, balance_after, description)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, transaction.getAccountId());
        stmt.setString(2, transaction.getTransactionType());
        stmt.setBigDecimal(3, transaction.getAmount());
        stmt.setBigDecimal(4, transaction.getBalanceAfter());
        stmt.setString(5, transaction.getDescription());

        int rows = stmt.executeUpdate();

        //System.out.println(">>> Rows Inserted = " + rows);

        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to save transaction.", e);
    }
}

    /**
     * Get transactions for one account
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM transactions
                WHERE account_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    public List<Transaction> getMiniStatement(int accountId) {

    List<Transaction> transactions = new ArrayList<>();

    String sql = """
            SELECT *
            FROM transactions
            WHERE account_id = ?
            ORDER BY created_at DESC
            LIMIT 5
            """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, accountId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            transactions.add(map(rs));
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return transactions;
}


    

    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    /**
     * Map ResultSet to Transaction
     */
    private Transaction map(ResultSet rs) throws SQLException {

        Transaction t = new Transaction();

        t.setTransactionId(rs.getInt("transaction_id"));
        t.setAccountId(rs.getInt("account_id"));
        t.setTransactionType(rs.getString("transaction_type"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setBalanceAfter(rs.getBigDecimal("balance_after"));
        t.setDescription(rs.getString("description"));

        if (rs.getTimestamp("created_at") != null) {
            t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return t;
    }
}