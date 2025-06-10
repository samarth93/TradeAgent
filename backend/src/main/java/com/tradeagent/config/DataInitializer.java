package com.tradeagent.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tradeagent.model.User;
import com.tradeagent.service.StockService;
import com.tradeagent.service.UserService;

/**
 * Data initializer to set up initial data when application starts
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        initializeStocks();
        initializeAdminUser();
    }

    private void initializeStocks() {
        try {
            stockService.initializeMockStocks();
            System.out.println("Mock stock data initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing stock data: " + e.getMessage());
        }
    }

    private void initializeAdminUser() {
        try {
            // Check if admin user already exists
            if (!userService.findByUsername("admin").isPresent()) {
                User admin = userService.createAdminUser(
                    "admin",
                    "admin@tradeagent.com",
                    "admin123",
                    "Admin",
                    "User"
                );
                admin.setBalance(BigDecimal.valueOf(100000)); // Give admin a large balance
                System.out.println("Admin user created successfully");
                System.out.println("Admin credentials - Username: admin, Password: admin123");
            }
        } catch (Exception e) {
            System.err.println("Error creating admin user: " + e.getMessage());
        }
    }
} 