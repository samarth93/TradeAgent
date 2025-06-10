package com.tradeagent.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

/**
 * Portfolio entity representing user's stock holdings
 * Tracks quantities of each stock owned by the user
 */
@Document(collection = "portfolios")
public class Portfolio {
    
    @Id
    private String id;
    
    @DBRef
    @NotNull(message = "User is required")
    private User user;
    
    // Map of stock symbol to quantity owned
    private Map<String, Integer> holdings = new HashMap<>();
    
    // Map of stock symbol to average purchase price
    private Map<String, BigDecimal> averagePrices = new HashMap<>();
    
    @LastModifiedDate
    private LocalDateTime lastUpdated;
    
    // Constructors
    public Portfolio() {}
    
    public Portfolio(User user) {
        this.user = user;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Map<String, Integer> getHoldings() {
        return holdings;
    }
    
    public void setHoldings(Map<String, Integer> holdings) {
        this.holdings = holdings;
    }
    
    public Map<String, BigDecimal> getAveragePrices() {
        return averagePrices;
    }
    
    public void setAveragePrices(Map<String, BigDecimal> averagePrices) {
        this.averagePrices = averagePrices;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Utility methods
    public void addStock(String symbol, Integer quantity, BigDecimal price) {
        Integer currentQuantity = holdings.getOrDefault(symbol, 0);
        BigDecimal currentAvgPrice = averagePrices.getOrDefault(symbol, BigDecimal.ZERO);
        
        // Calculate new average price
        if (currentQuantity > 0) {
            BigDecimal totalValue = currentAvgPrice.multiply(BigDecimal.valueOf(currentQuantity))
                                  .add(price.multiply(BigDecimal.valueOf(quantity)));
            Integer newQuantity = currentQuantity + quantity;
            BigDecimal newAvgPrice = totalValue.divide(BigDecimal.valueOf(newQuantity), 2, BigDecimal.ROUND_HALF_UP);
            averagePrices.put(symbol, newAvgPrice);
        } else {
            averagePrices.put(symbol, price);
        }
        
        holdings.put(symbol, currentQuantity + quantity);
    }
    
    public boolean removeStock(String symbol, Integer quantity) {
        Integer currentQuantity = holdings.getOrDefault(symbol, 0);
        
        if (currentQuantity < quantity) {
            return false; // Not enough shares to sell
        }
        
        Integer newQuantity = currentQuantity - quantity;
        if (newQuantity == 0) {
            holdings.remove(symbol);
            averagePrices.remove(symbol);
        } else {
            holdings.put(symbol, newQuantity);
        }
        
        return true;
    }
    
    public Integer getQuantity(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }
    
    public BigDecimal getAveragePrice(String symbol) {
        return averagePrices.getOrDefault(symbol, BigDecimal.ZERO);
    }
    
    public boolean hasStock(String symbol) {
        return holdings.containsKey(symbol) && holdings.get(symbol) > 0;
    }
    
    public BigDecimal calculateTotalValue(Map<String, BigDecimal> currentPrices) {
        BigDecimal totalValue = BigDecimal.ZERO;
        
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal currentPrice = currentPrices.getOrDefault(symbol, BigDecimal.ZERO);
            
            totalValue = totalValue.add(currentPrice.multiply(BigDecimal.valueOf(quantity)));
        }
        
        return totalValue;
    }
} 