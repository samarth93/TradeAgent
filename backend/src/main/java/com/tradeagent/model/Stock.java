package com.tradeagent.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Stock entity representing available stocks for trading
 * Contains stock information and current market price with real-time data
 */
@Document(collection = "stocks")
public class Stock {
    
    @Id
    private String id;
    
    @NotBlank(message = "Stock symbol is required")
    @Indexed(unique = true)
    private String symbol;
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotNull(message = "Current price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal currentPrice;
    
    @NotNull(message = "Previous close price is required")
    @DecimalMin(value = "0.01", message = "Previous close must be greater than 0")
    private BigDecimal previousClose;
    
    private BigDecimal changeAmount;
    private BigDecimal changePercent;
    
    // Additional real-time fields
    private BigDecimal openPrice;
    private BigDecimal dayHigh;
    private BigDecimal dayLow;
    
    @NotBlank(message = "Sector is required")
    private String sector;
    
    @NotBlank(message = "Industry is required")
    private String industry;
    
    private String description;
    
    @LastModifiedDate
    private LocalDateTime lastUpdated;
    
    // Constructors
    public Stock() {}
    
    public Stock(String symbol, String companyName, BigDecimal currentPrice, 
                BigDecimal previousClose, String sector, String industry) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.previousClose = previousClose;
        this.sector = sector;
        this.industry = industry;
        calculateChange();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        calculateChange();
    }
    
    public BigDecimal getPreviousClose() {
        return previousClose;
    }
    
    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
        calculateChange();
    }
    
    public BigDecimal getChangeAmount() {
        return changeAmount;
    }
    
    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }
    
    public BigDecimal getChangePercent() {
        return changePercent;
    }
    
    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }
    
    public BigDecimal getOpenPrice() {
        return openPrice;
    }
    
    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }
    
    public BigDecimal getDayHigh() {
        return dayHigh;
    }
    
    public void setDayHigh(BigDecimal dayHigh) {
        this.dayHigh = dayHigh;
    }
    
    public BigDecimal getDayLow() {
        return dayLow;
    }
    
    public void setDayLow(BigDecimal dayLow) {
        this.dayLow = dayLow;
    }
    
    public String getSector() {
        return sector;
    }
    
    public void setSector(String sector) {
        this.sector = sector;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Utility methods
    private void calculateChange() {
        if (currentPrice != null && previousClose != null && 
            previousClose.compareTo(BigDecimal.ZERO) > 0) {
            changeAmount = currentPrice.subtract(previousClose);
            changePercent = changeAmount.divide(previousClose, 4, BigDecimal.ROUND_HALF_UP)
                          .multiply(BigDecimal.valueOf(100));
        }
    }
    
    public boolean isPositiveChange() {
        return changeAmount != null && changeAmount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean isNegativeChange() {
        return changeAmount != null && changeAmount.compareTo(BigDecimal.ZERO) < 0;
    }
} 