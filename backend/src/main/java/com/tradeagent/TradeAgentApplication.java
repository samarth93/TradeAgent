package com.tradeagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main Spring Boot Application class for TradeAgent
 * Enables MongoDB auditing for automatic timestamp management
 */
@SpringBootApplication
@EnableMongoAuditing
public class TradeAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeAgentApplication.class, args);
    }
} 