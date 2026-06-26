package com.akhil.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.model.Transaction;

/**
 * Repository for Transaction data access operations
 */
public class TransactionRepository {
    
    /**
     * Create a new transaction
     */
    public boolean createTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (account_id, transaction_type, amount, description, status, transaction_date) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getDescription());
            stmt.setString(5, transaction.getStatus());
            stmt.setTimestamp(6, Timestamp.valueOf(transaction.getTransactionDate()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create transaction", e);
        }
    }
    
    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch transaction by id", e);
        }
        return null;
    }
    
    /**
     * Get transactions by account ID
     */
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch transactions by account id", e);
        }
        return transactions;
    }
    
    /**
     * Update transaction status
     */
    public boolean updateTransactionStatus(int transactionId, String status) {
        String query = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, transactionId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update transaction status", e);
        }
    }
    
    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch all transactions", e);
        }
        return transactions;
    }
    
    /**
     * Map ResultSet to Transaction object
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setDescription(rs.getString("description"));
        transaction.setStatus(rs.getString("status"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date") != null ? 
                                      rs.getTimestamp("transaction_date").toLocalDateTime() : null);
        transaction.setCreatedAt(rs.getTimestamp("created_at"));
        return transaction;
    }
}
