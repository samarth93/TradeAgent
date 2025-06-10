package com.tradeagent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tradeagent.model.Stock;

/**
 * Repository interface for Stock entity operations
 * Provides CRUD operations and custom query methods for stock data
 */
@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
    
    /**
     * Find stock by symbol
     * @param symbol the stock symbol to search for
     * @return Optional containing the stock if found
     */
    Optional<Stock> findBySymbol(String symbol);
    
    /**
     * Find stocks by sector
     * @param sector the sector to search for
     * @return List of stocks in the sector
     */
    List<Stock> findBySector(String sector);
    
    /**
     * Find stocks by industry
     * @param industry the industry to search for
     * @return List of stocks in the industry
     */
    List<Stock> findByIndustry(String industry);
    
    /**
     * Check if stock symbol exists
     * @param symbol the symbol to check
     * @return true if symbol exists, false otherwise
     */
    boolean existsBySymbol(String symbol);
} 