package com.tradeagent.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeagent.model.User;
import com.tradeagent.security.UserPrincipal;
import com.tradeagent.service.TradingService;
import com.tradeagent.service.UserService;

/**
 * REST Controller for portfolio operations
 * Handles portfolio data retrieval and management
 */
@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {
    
    @Autowired
    private TradingService tradingService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get user's portfolio
     * @return portfolio details
     */
    @GetMapping
    public ResponseEntity<?> getUserPortfolio() {
        try {
            User user = getCurrentUser();
            TradingService.PortfolioSummary summary = tradingService.getPortfolioSummary(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("portfolio", summary.getPortfolio());
            response.put("portfolioValue", summary.getPortfolioValue());
            response.put("cashBalance", summary.getCashBalance());
            response.put("totalValue", summary.getTotalValue());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get portfolio summary
     * @return portfolio summary with values
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getPortfolioSummary() {
        try {
            User user = getCurrentUser();
            TradingService.PortfolioSummary summary = tradingService.getPortfolioSummary(user);
            
            return ResponseEntity.ok(summary);
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
} 