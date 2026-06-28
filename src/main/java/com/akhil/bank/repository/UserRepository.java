package com.akhil.bank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.model.User;

/**
 * Repository for User database operations.
 */
public class UserRepository {

    /**
     * Save a new user and return the generated user ID.
     */
    public int saveUser(User user) {

        String sql = "INSERT INTO users(name, email, phone, password) VALUES (?, ?, ?, ?) RETURNING id";

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
            throw new RuntimeException("Failed to save user.", e);
        }

        return -1;
    }

    /**
     * Check whether an email is already registered.
     */
    public boolean emailExists(String email) {

        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check email.", e);
        }
    }

    /**
     * Find a user by email.
     */
    public User findByEmail(String email) {

        String sql = "SELECT id, name, email, phone, password FROM users WHERE email = ?";

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
            throw new RuntimeException("Failed to fetch user.", e);
        }

        return null;
    }

    public int saveUser(Connection connection, User user) {

    String sql = """
        INSERT INTO users(name, email, phone, password)
        VALUES (?, ?, ?, ?)
        RETURNING id
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPhone());
        statement.setString(4, user.getPassword());

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }

    } catch (SQLException e) {
        throw new RuntimeException("Failed to save user.", e);
    }

    return -1;
}

}