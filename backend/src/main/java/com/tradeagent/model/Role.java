package com.tradeagent.model;

/**
 * Enum representing user roles in the trading system
 * TRADER: Regular users who can trade stocks
 * ADMIN: Administrative users with additional privileges
 */
public enum Role {
    TRADER("ROLE_TRADER"),
    ADMIN("ROLE_ADMIN");
    
    private final String authority;
    
    Role(String authority) {
        this.authority = authority;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    @Override
    public String toString() {
        return authority;
    }
} 