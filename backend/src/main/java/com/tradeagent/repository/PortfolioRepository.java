package com.tradeagent.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tradeagent.model.Portfolio;
import com.tradeagent.model.User;

/**
 * Repository interface for Portfolio entity operations
 * Provides CRUD operations and custom query methods for user portfolios
 */
@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
    
    /**
     * Find portfolio by user
     * @param user the user to find portfolio for
     * @return Optional containing the portfolio if found
     */
    Optional<Portfolio> findByUser(User user);
    
    /**
     * Check if portfolio exists for user
     * @param user the user to check
     * @return true if portfolio exists, false otherwise
     */
    boolean existsByUser(User user);
} 