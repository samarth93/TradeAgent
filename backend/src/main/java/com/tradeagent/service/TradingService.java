package com.tradeagent.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradeagent.model.Portfolio;
import com.tradeagent.model.Stock;
import com.tradeagent.model.Transaction;
import com.tradeagent.model.TransactionType;
import com.tradeagent.model.User;
import com.tradeagent.repository.PortfolioRepository;
import com.tradeagent.repository.TransactionRepository;

/**
 * Service class for trading operations
 * Handles buy/sell transactions and portfolio management
 */
@Service
@Transactional
public class TradingService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StockService stockService;
    
    /**
     * Execute buy order
     * @param user the user making the purchase
     * @param stockSymbol the stock symbol
     * @param quantity the quantity to buy
     * @return the transaction record
     */
    public Transaction buyStock(User user, String stockSymbol, Integer quantity) {
        // Validate stock exists
        if (!stockService.stockExists(stockSymbol)) {
            throw new RuntimeException("Stock not found: " + stockSymbol);
        }
        
        // Get current stock price
        BigDecimal currentPrice = stockService.getCurrentPrice(stockSymbol);
        BigDecimal totalCost = currentPrice.multiply(BigDecimal.valueOf(quantity));
        
        // Check if user has sufficient balance
        if (!userService.hasSufficientBalance(user, totalCost)) {
            throw new RuntimeException("Insufficient balance for this transaction");
        }
        
        // Get stock details
        Stock stock = stockService.findBySymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        // Deduct amount from user balance
        userService.deductBalance(user, totalCost);
        
        // Update portfolio
        Portfolio portfolio = getOrCreatePortfolio(user);
        portfolio.addStock(stockSymbol, quantity, currentPrice);
        portfolioRepository.save(portfolio);
        
        // Create transaction record
        Transaction transaction = new Transaction(user, stockSymbol, stock.getCompanyName(),
                TransactionType.BUY, quantity, currentPrice);
        transaction.setNotes("Buy order executed successfully");
        
        return transactionRepository.save(transaction);
    }
    
    /**
     * Execute sell order
     * @param user the user making the sale
     * @param stockSymbol the stock symbol
     * @param quantity the quantity to sell
     * @return the transaction record
     */
    public Transaction sellStock(User user, String stockSymbol, Integer quantity) {
        // Validate stock exists
        if (!stockService.stockExists(stockSymbol)) {
            throw new RuntimeException("Stock not found: " + stockSymbol);
        }
        
        // Get user's portfolio
        Portfolio portfolio = getOrCreatePortfolio(user);
        
        // Check if user has enough shares to sell
        if (!portfolio.hasStock(stockSymbol) || portfolio.getQuantity(stockSymbol) < quantity) {
            throw new RuntimeException("Insufficient shares to sell");
        }
        
        // Get current stock price
        BigDecimal currentPrice = stockService.getCurrentPrice(stockSymbol);
        BigDecimal totalValue = currentPrice.multiply(BigDecimal.valueOf(quantity));
        
        // Get stock details
        Stock stock = stockService.findBySymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        // Remove shares from portfolio
        portfolio.removeStock(stockSymbol, quantity);
        portfolioRepository.save(portfolio);
        
        // Add amount to user balance
        userService.addBalance(user, totalValue);
        
        // Create transaction record
        Transaction transaction = new Transaction(user, stockSymbol, stock.getCompanyName(),
                TransactionType.SELL, quantity, currentPrice);
        transaction.setNotes("Sell order executed successfully");
        
        return transactionRepository.save(transaction);
    }
    
    /**
     * Get user's transaction history
     * @param user the user
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory(User user) {
        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }
    
    /**
     * Get user's portfolio
     * @param user the user
     * @return the user's portfolio
     */
    public Portfolio getUserPortfolio(User user) {
        return getOrCreatePortfolio(user);
    }
    
    /**
     * Get or create portfolio for user
     * @param user the user
     * @return the portfolio
     */
    private Portfolio getOrCreatePortfolio(User user) {
        Optional<Portfolio> existingPortfolio = portfolioRepository.findByUser(user);
        
        if (existingPortfolio.isPresent()) {
            return existingPortfolio.get();
        } else {
            Portfolio newPortfolio = new Portfolio(user);
            return portfolioRepository.save(newPortfolio);
        }
    }
    
    /**
     * Calculate portfolio total value
     * @param user the user
     * @return the total portfolio value
     */
    public BigDecimal calculatePortfolioValue(User user) {
        Portfolio portfolio = getUserPortfolio(user);
        
        BigDecimal totalValue = BigDecimal.ZERO;
        for (String symbol : portfolio.getHoldings().keySet()) {
            Integer quantity = portfolio.getQuantity(symbol);
            BigDecimal currentPrice = stockService.getCurrentPrice(symbol);
            totalValue = totalValue.add(currentPrice.multiply(BigDecimal.valueOf(quantity)));
        }
        
        return totalValue;
    }
    
    /**
     * Get portfolio summary including cash balance
     * @param user the user
     * @return portfolio summary
     */
    public PortfolioSummary getPortfolioSummary(User user) {
        Portfolio portfolio = getUserPortfolio(user);
        BigDecimal portfolioValue = calculatePortfolioValue(user);
        BigDecimal cashBalance = user.getBalance();
        BigDecimal totalValue = portfolioValue.add(cashBalance);
        
        return new PortfolioSummary(portfolio, portfolioValue, cashBalance, totalValue);
    }
    
    /**
     * Inner class for portfolio summary
     */
    public static class PortfolioSummary {
        private Portfolio portfolio;
        private BigDecimal portfolioValue;
        private BigDecimal cashBalance;
        private BigDecimal totalValue;
        
        public PortfolioSummary(Portfolio portfolio, BigDecimal portfolioValue, 
                              BigDecimal cashBalance, BigDecimal totalValue) {
            this.portfolio = portfolio;
            this.portfolioValue = portfolioValue;
            this.cashBalance = cashBalance;
            this.totalValue = totalValue;
        }
        
        // Getters
        public Portfolio getPortfolio() { return portfolio; }
        public BigDecimal getPortfolioValue() { return portfolioValue; }
        public BigDecimal getCashBalance() { return cashBalance; }
        public BigDecimal getTotalValue() { return totalValue; }
    }
} 