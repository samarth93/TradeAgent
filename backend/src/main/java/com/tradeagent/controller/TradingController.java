package com.tradeagent.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeagent.model.Transaction;
import com.tradeagent.model.User;
import com.tradeagent.security.UserPrincipal;
import com.tradeagent.service.TradingService;
import com.tradeagent.service.UserService;

import jakarta.validation.Valid;

/**
 * REST Controller for trading operations
 * Handles buy/sell transactions and trading history
 */
@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "http://localhost:3000")
public class TradingController {
    
    @Autowired
    private TradingService tradingService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Buy stock endpoint
     * @param buyRequest the buy request
     * @return response with transaction details
     */
    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@Valid @RequestBody BuyRequest buyRequest) {
        try {
            User user = getCurrentUser();
            
            Transaction transaction = tradingService.buyStock(
                user,
                buyRequest.getStockSymbol(),
                buyRequest.getQuantity()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock purchased successfully!");
            response.put("transaction", transaction);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Sell stock endpoint
     * @param sellRequest the sell request
     * @return response with transaction details
     */
    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@Valid @RequestBody SellRequest sellRequest) {
        try {
            User user = getCurrentUser();
            
            Transaction transaction = tradingService.sellStock(
                user,
                sellRequest.getStockSymbol(),
                sellRequest.getQuantity()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock sold successfully!");
            response.put("transaction", transaction);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get transaction history
     * @return list of user transactions
     */
    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory() {
        try {
            User user = getCurrentUser();
            List<Transaction> transactions = tradingService.getTransactionHistory(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactions);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get current authenticated user
     * @return current user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        return userService.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Buy request DTO
     */
    public static class BuyRequest {
        private String stockSymbol;
        private Integer quantity;
        
        // Getters and setters
        public String getStockSymbol() { return stockSymbol; }
        public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    /**
     * Sell request DTO
     */
    public static class SellRequest {
        private String stockSymbol;
        private Integer quantity;
        
        // Getters and setters
        public String getStockSymbol() { return stockSymbol; }
        public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
} 