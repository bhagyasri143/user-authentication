package com.auth.user.service;

import com.auth.user.entities.User;
import com.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        // Check if user already exists
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }

        // Encrypt password using BCrypt
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Save the user in the database
        userRepository.save(user);
    }

    public User login(String username, String password) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Check if the provided password matches the encrypted password in the database
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Login successful
        return user;
    }
}
