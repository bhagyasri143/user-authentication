package com.auth.user.repository;

import com.auth.user.entities.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final HikariDataSource dataSource;

    public UserRepository() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/spring");
        config.setUsername("root");
        config.setPassword("bhagi");
        config.setMaximumPoolSize(3);
        dataSource = new HikariDataSource(config);
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    logger.info("User found by username: {}", username);
                    return user;
                }
                logger.info("User not found by username: {}", username);
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username", e);
            throw new RuntimeException("Error finding user by username", e);
        }
    }

    public int save(User user) {
        String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isEnabled());
            int result = stmt.executeUpdate();
            logger.info("User saved with id: {}", user.getId());
            return result;
        } catch (SQLException e) {
            logger.error("Error saving user", e);
            throw new RuntimeException("Error saving user", e);
        }
    }

    public User findById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id=?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    logger.info("User found by id: {}", id);
                    return user;
                } else {
                    logger.info("User not found by id: {}", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting user by id", e);
            throw new RuntimeException("Error getting user by id", e);
        }
    }

    public void deleteById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No user found with id: {}", id);
            } else {
                logger.info("User deleted by id: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
            throw new RuntimeException("Error deleting user by id", e);
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error getting all users", e);
            throw new RuntimeException("Error getting all users", e);
        }
        logger.info("Found {} users", users.size());
        return users;
    }
}


