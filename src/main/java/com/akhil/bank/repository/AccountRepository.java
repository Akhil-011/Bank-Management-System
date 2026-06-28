package com.akhil.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.model.Account;

/**
 * Repository for Account data access operations
 */
public class AccountRepository {
    
    /**
     * Create a new account
     */
    public boolean createAccount(Account account) {
        String query = "INSERT INTO accounts (user_id, account_number, account_type, balance, currency, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, account.getUserId());
            stmt.setString(2, account.getAccountNumber());
            stmt.setString(3, account.getAccountType());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getCurrency());
            stmt.setString(6, account.getStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
    e.printStackTrace();
    throw new IllegalStateException("Failed to create account", e);
}
    }
    
    /**
     * Get account by ID
     */
    public Account getAccountById(int accountId) {
        String query = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch account by id", e);
        }
        return null;
    }
    
    /**
     * Get accounts by user ID
     */
    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch accounts by user id", e);
        }
        return accounts;
    }


    public Account getAccountByUserId(int userId) {

    String query = "SELECT * FROM accounts WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, userId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapResultSetToAccount(rs);
        }

    } catch (SQLException e) {
        throw new IllegalStateException("Failed to fetch account.", e);
    }

    return null;
}



public boolean updateBalance(int accountId, java.math.BigDecimal balance) {

    String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setBigDecimal(1, balance);
        stmt.setInt(2, accountId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        throw new RuntimeException("Failed to update balance.", e);
    }
}
    
public boolean updateBalance(Connection conn, int accountId, java.math.BigDecimal balance) {

    String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setBigDecimal(1, balance);
        stmt.setInt(2, accountId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        throw new RuntimeException("Failed to update balance.", e);
    }
}

public Connection getConnection() {
    return DatabaseConnection.getConnection();
}

public Account getAccountByAccountNumber(String accountNumber) {

    String sql = "SELECT * FROM accounts WHERE account_number = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, accountNumber);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapResultSetToAccount(rs);
        }

    } catch (SQLException e) {
        throw new RuntimeException("Failed to fetch account.", e);
    }

    return null;
}

    /**
     * Update account
     */
    public boolean updateAccount(Account account) {
        String query = "UPDATE accounts SET account_type = ?, balance = ?, status = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, account.getAccountType());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setString(3, account.getStatus());
            stmt.setInt(4, account.getAccountId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update account", e);
        }
    }
    
    /**
     * Delete account
     */
    public boolean deleteAccount(int accountId) {
        String query = "DELETE FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete account", e);
        }
    }
    
    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch all accounts", e);
        }
        return accounts;
    }
    
    /**
     * Map ResultSet to Account object
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCurrency(rs.getString("currency"));
        account.setStatus(rs.getString("status"));
        account.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                            rs.getTimestamp("created_at").toLocalDateTime() : null);
        account.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                            rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return account;
    }

    public boolean accountNumberExists(String accountNumber) {

    String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, accountNumber);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return false;
}

public boolean createAccount(Connection connection, Account account) {

    String query = """
        INSERT INTO accounts
        (user_id, account_number, account_type, balance, currency, status)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement stmt =
            connection.prepareStatement(query)) {

        stmt.setInt(1, account.getUserId());
        stmt.setString(2, account.getAccountNumber());
        stmt.setString(3, account.getAccountType());
        stmt.setBigDecimal(4, account.getBalance());
        stmt.setString(5, account.getCurrency());
        stmt.setString(6, account.getStatus());

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        throw new RuntimeException("Failed to create account.", e);
    }
}

}
