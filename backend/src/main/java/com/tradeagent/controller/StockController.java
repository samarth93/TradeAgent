package com.tradeagent.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeagent.model.Stock;
import com.tradeagent.service.StockService;

/**
 * REST Controller for stock operations
 * Handles stock data retrieval and management
 */
@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
    
    @Autowired
    private StockService stockService;
    
    /**
     * Get all available stocks
     * @return list of all stocks
     */
    @GetMapping
    public ResponseEntity<?> getAllStocks() {
        try {
            List<Stock> stocks = stockService.getAllStocks();
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve stocks");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get stock by symbol
     * @param symbol the stock symbol
     * @return stock details
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<?> getStockBySymbol(@PathVariable String symbol) {
        try {
            Stock stock = stockService.findBySymbol(symbol)
                    .orElseThrow(() -> new RuntimeException("Stock not found"));
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get stocks by sector
     * @param sector the sector name
     * @return list of stocks in the sector
     */
    @GetMapping("/sector/{sector}")
    public ResponseEntity<?> getStocksBySector(@PathVariable String sector) {
        try {
            List<Stock> stocks = stockService.getStocksBySector(sector);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve stocks by sector");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Initialize mock stock data (for development)
     * @return success message
     */
    @PostMapping("/init")
    public ResponseEntity<?> initializeStocks() {
        try {
            stockService.initializeMockStocks();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Mock stocks initialized successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to initialize stocks");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update stock prices (for simulation)
     * @return success message
     */
    @PostMapping("/update-prices")
    public ResponseEntity<?> updateStockPrices() {
        try {
            stockService.updateStockPrices();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Stock prices updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update stock prices");
            return ResponseEntity.badRequest().body(error);
        }
    }
} 