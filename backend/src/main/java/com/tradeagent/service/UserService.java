package com.tradeagent.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tradeagent.model.Role;
import com.tradeagent.model.User;
import com.tradeagent.repository.UserRepository;

/**
 * Service class for User entity operations
 * Handles user registration, authentication, and management
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     * @param username the username
     * @param email the email
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @return the created user
     */
    public User registerUser(String username, String email, String password, 
                           String firstName, String lastName) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }
        
        // Create new user
        User user = new User(username, email, passwordEncoder.encode(password), 
                           firstName, lastName);
        user.setBalance(BigDecimal.valueOf(10000)); // Initial balance
        
        return userRepository.save(user);
    }
    
    /**
     * Create admin user
     * @param username the username
     * @param email the email
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @return the created admin user
     */
    public User createAdminUser(String username, String email, String password,
                              String firstName, String lastName) {
        User user = registerUser(username, email, password, firstName, lastName);
        user.addRole(Role.ADMIN);
        return userRepository.save(user);
    }
    
    /**
     * Find user by username
     * @param username the username
     * @return Optional containing the user if found
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find user by email
     * @param email the email
     * @return Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find user by ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get all users (admin only)
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Update user balance
     * @param userId the user ID
     * @param newBalance the new balance
     * @return the updated user
     */
    public User updateUserBalance(String userId, BigDecimal newBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setBalance(newBalance);
        return userRepository.save(user);
    }
    
    /**
     * Delete user (admin only)
     * @param userId the user ID
     */
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }
    
    /**
     * Check if user has sufficient balance
     * @param user the user
     * @param amount the amount to check
     * @return true if sufficient balance, false otherwise
     */
    public boolean hasSufficientBalance(User user, BigDecimal amount) {
        return user.getBalance().compareTo(amount) >= 0;
    }
    
    /**
     * Deduct amount from user balance
     * @param user the user
     * @param amount the amount to deduct
     * @return the updated user
     */
    public User deductBalance(User user, BigDecimal amount) {
        if (!hasSufficientBalance(user, amount)) {
            throw new RuntimeException("Insufficient balance");
        }
        
        user.setBalance(user.getBalance().subtract(amount));
        return userRepository.save(user);
    }
    
    /**
     * Add amount to user balance
     * @param user the user
     * @param amount the amount to add
     * @return the updated user
     */
    public User addBalance(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        return userRepository.save(user);
    }
} 