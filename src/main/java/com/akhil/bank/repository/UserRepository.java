package com.akhil.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.model.User;

/**
 * Repository for User data access operations
 */
public class UserRepository {

    public int saveUser(User user) {
        String sql = "INSERT INTO users(name,email,phone,password) VALUES(?,?,?,?) RETURNING id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user : " + e.getMessage(), e);
        }

        return -1;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    
    /**
     * Create a new user
     */
    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, email, password_hash, first_name, last_name, phone_number, address) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAddress());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create user", e);
        }
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch user by id", e);
        }
        return null;
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch user by username", e);
        }
        return null;
    }
    
    /**
     * Update user
     */
    public boolean updateUser(User user) {
        String query = "UPDATE users SET email = ?, first_name = ?, last_name = ?, phone_number = ?, address = ? " +
                      "WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setInt(6, user.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user", e);
        }
    }
    
    /**
     * Delete user
     */
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete user", e);
        }
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch all users", e);
        }
        return users;
    }

    public boolean userExistsByEmail(String email) {
        return emailExists(email);
    }

    public User getUserByEmail(String email) {
        return findByEmail(email);
    }

    public boolean createUserLegacy(User user) {
        return createUser(user);
    }
    
    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                         rs.getTimestamp("created_at").toLocalDateTime() : null);
        user.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                         rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return user;
    }
}
