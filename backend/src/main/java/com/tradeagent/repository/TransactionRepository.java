package com.tradeagent.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tradeagent.model.Transaction;
import com.tradeagent.model.User;

/**
 * Repository interface for Transaction entity operations
 * Provides CRUD operations and custom query methods for trading history
 */
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    /**
     * Find all transactions for a specific user
     * @param user the user to find transactions for
     * @return List of transactions for the user
     */
    List<Transaction> findByUserOrderByTransactionDateDesc(User user);
    
    /**
     * Find transactions for a user within a date range
     * @param user the user to find transactions for
     * @param startDate the start date
     * @param endDate the end date
     * @return List of transactions within the date range
     */
    List<Transaction> findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(
            User user, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find transactions for a specific stock symbol
     * @param stockSymbol the stock symbol to search for
     * @return List of transactions for the stock
     */
    List<Transaction> findByStockSymbolOrderByTransactionDateDesc(String stockSymbol);
    
    /**
     * Find transactions for a user and specific stock
     * @param user the user to find transactions for
     * @param stockSymbol the stock symbol to search for
     * @return List of transactions for the user and stock
     */
    List<Transaction> findByUserAndStockSymbolOrderByTransactionDateDesc(User user, String stockSymbol);
} 