package com.tradeagent.model;

/**
 * Enum representing transaction types in the trading system
 * BUY: Purchase of stocks
 * SELL: Sale of stocks
 */
public enum TransactionType {
    BUY("BUY"),
    SELL("SELL");
    
    private final String value;
    
    TransactionType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
} 