package com.tradeagent.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tradeagent.model.Stock;
import com.tradeagent.repository.StockRepository;

/**
 * Service class for Stock entity operations
 * Handles stock data management with real-time Finnhub API integration
 */
@Service
public class StockService {
    
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private FinnhubService finnhubService;
    
    private final Random random = new Random();
    
    /**
     * Get all available stocks with real-time prices
     * @return List of all stocks with updated prices
     */
    public List<Stock> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        
        // Update prices with real-time data
        for (Stock stock : stocks) {
            updateStockWithRealTimeData(stock);
        }
        
        return stocks;
    }
    
    /**
     * Find stock by symbol with real-time price update
     * @param symbol the stock symbol
     * @return Optional containing the stock if found
     */
    public Optional<Stock> findBySymbol(String symbol) {
        Optional<Stock> stockOpt = stockRepository.findBySymbol(symbol);
        
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            updateStockWithRealTimeData(stock);
            return Optional.of(stock);
        }
        
        return stockOpt;
    }
    
    /**
     * Update stock with real-time data from Finnhub API
     * @param stock the stock to update
     */
    private void updateStockWithRealTimeData(Stock stock) {
        try {
            Map<String, Object> realTimeData = finnhubService.getStockQuote(stock.getSymbol());
            
            if (realTimeData != null && realTimeData.containsKey("currentPrice")) {
                BigDecimal newPrice = (BigDecimal) realTimeData.get("currentPrice");
                BigDecimal previousClose = (BigDecimal) realTimeData.get("previousClose");
                
                // Only update if we got valid data
                if (newPrice.compareTo(BigDecimal.ZERO) > 0) {
                    stock.setPreviousClose(previousClose);
                    stock.setCurrentPrice(newPrice);
                    
                    // Update additional fields if available
                    if (realTimeData.containsKey("highPrice")) {
                        stock.setDayHigh((BigDecimal) realTimeData.get("highPrice"));
                    }
                    if (realTimeData.containsKey("lowPrice")) {
                        stock.setDayLow((BigDecimal) realTimeData.get("lowPrice"));
                    }
                    if (realTimeData.containsKey("openPrice")) {
                        stock.setOpenPrice((BigDecimal) realTimeData.get("openPrice"));
                    }
                    
                    logger.info("Updated {} with real-time price: {}", stock.getSymbol(), newPrice);
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to update real-time data for {}: {}", stock.getSymbol(), e.getMessage());
            // Continue with existing data if API call fails
        }
    }
    
    /**
     * Get stocks by sector
     * @param sector the sector
     * @return List of stocks in the sector
     */
    public List<Stock> getStocksBySector(String sector) {
        return stockRepository.findBySector(sector);
    }
    
    /**
     * Get stocks by industry
     * @param industry the industry
     * @return List of stocks in the industry
     */
    public List<Stock> getStocksByIndustry(String industry) {
        return stockRepository.findByIndustry(industry);
    }
    
    /**
     * Initialize stock data with real company information
     */
    public void initializeMockStocks() {
        if (stockRepository.count() == 0) {
            logger.info("Initializing stock data with real-time prices from Finnhub...");
            
            createStockWithRealTimeData("AAPL", "Apple Inc.", "Technology", "Consumer Electronics");
            createStockWithRealTimeData("GOOGL", "Alphabet Inc.", "Technology", "Internet Content & Information");
            createStockWithRealTimeData("MSFT", "Microsoft Corporation", "Technology", "Software");
            createStockWithRealTimeData("AMZN", "Amazon.com Inc.", "Consumer Discretionary", "Internet & Direct Marketing Retail");
            createStockWithRealTimeData("TSLA", "Tesla Inc.", "Consumer Discretionary", "Automobiles");
            createStockWithRealTimeData("META", "Meta Platforms Inc.", "Technology", "Social Media");
            createStockWithRealTimeData("NVDA", "NVIDIA Corporation", "Technology", "Semiconductors");
            createStockWithRealTimeData("NFLX", "Netflix Inc.", "Communication Services", "Entertainment");
            createStockWithRealTimeData("AMD", "Advanced Micro Devices Inc.", "Technology", "Semiconductors");
            createStockWithRealTimeData("INTC", "Intel Corporation", "Technology", "Semiconductors");
        }
    }
    
    /**
     * Create a stock with real-time data from Finnhub
     * @param symbol the stock symbol
     * @param companyName the company name
     * @param sector the sector
     * @param industry the industry
     */
    private void createStockWithRealTimeData(String symbol, String companyName, 
                                           String sector, String industry) {
        try {
            Map<String, Object> realTimeData = finnhubService.getStockQuote(symbol);
            
            BigDecimal currentPrice;
            BigDecimal previousClose;
            
            if (realTimeData != null && realTimeData.containsKey("currentPrice")) {
                currentPrice = (BigDecimal) realTimeData.get("currentPrice");
                previousClose = (BigDecimal) realTimeData.get("previousClose");
            } else {
                // Fallback to default prices if API fails
                currentPrice = getDefaultPrice(symbol);
                previousClose = currentPrice.multiply(BigDecimal.valueOf(0.98));
            }
            
            Stock stock = new Stock(symbol, companyName, currentPrice, previousClose, sector, industry);
            stock.setDescription("Real-time stock data for " + companyName);
            
            // Set additional fields if available
            if (realTimeData != null) {
                if (realTimeData.containsKey("highPrice")) {
                    stock.setDayHigh((BigDecimal) realTimeData.get("highPrice"));
                }
                if (realTimeData.containsKey("lowPrice")) {
                    stock.setDayLow((BigDecimal) realTimeData.get("lowPrice"));
                }
                if (realTimeData.containsKey("openPrice")) {
                    stock.setOpenPrice((BigDecimal) realTimeData.get("openPrice"));
                }
            }
            
            stockRepository.save(stock);
            logger.info("Created stock {} with real-time price: {}", symbol, currentPrice);
            
        } catch (Exception e) {
            logger.error("Error creating stock {} with real-time data: {}", symbol, e.getMessage());
            // Create with fallback data
            createFallbackStock(symbol, companyName, sector, industry);
        }
    }
    
    /**
     * Create stock with fallback data if API fails
     */
    private void createFallbackStock(String symbol, String companyName, String sector, String industry) {
        BigDecimal basePrice = getDefaultPrice(symbol);
        BigDecimal currentPrice = basePrice.multiply(BigDecimal.valueOf(1 + (random.nextDouble() - 0.5) * 0.1))
                                          .setScale(2, RoundingMode.HALF_UP);
        
        Stock stock = new Stock(symbol, companyName, currentPrice, basePrice, sector, industry);
        stock.setDescription("Fallback stock data for " + companyName);
        stockRepository.save(stock);
        
        logger.warn("Created stock {} with fallback data", symbol);
    }
    
    /**
     * Get default price for fallback
     */
    private BigDecimal getDefaultPrice(String symbol) {
        switch (symbol) {
            case "AAPL": return new BigDecimal("150.00");
            case "GOOGL": return new BigDecimal("2500.00");
            case "MSFT": return new BigDecimal("300.00");
            case "AMZN": return new BigDecimal("3200.00");
            case "TSLA": return new BigDecimal("800.00");
            case "META": return new BigDecimal("320.00");
            case "NVDA": return new BigDecimal("450.00");
            case "NFLX": return new BigDecimal("400.00");
            case "AMD": return new BigDecimal("100.00");
            case "INTC": return new BigDecimal("50.00");
            default: return new BigDecimal("100.00");
        }
    }
    
    /**
     * Update stock prices with real-time data
     */
    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        
        for (Stock stock : stocks) {
            updateStockWithRealTimeData(stock);
            stockRepository.save(stock);
        }
        
        logger.info("Updated {} stocks with real-time prices", stocks.size());
    }
    
    /**
     * Get current stock price with real-time update
     * @param symbol the stock symbol
     * @return the current price
     */
    public BigDecimal getCurrentPrice(String symbol) {
        Optional<Stock> stockOpt = findBySymbol(symbol);
        return stockOpt.map(Stock::getCurrentPrice)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + symbol));
    }
    
    /**
     * Check if stock exists
     * @param symbol the stock symbol
     * @return true if stock exists, false otherwise
     */
    public boolean stockExists(String symbol) {
        return stockRepository.existsBySymbol(symbol);
    }
} 