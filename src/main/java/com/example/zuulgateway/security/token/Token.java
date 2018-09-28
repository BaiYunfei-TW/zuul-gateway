package com.example.zuulgateway.security.token;

import java.util.List;

public class Token {
    private String userId;
    private List<String> role;

    public Token() {
    }

    public Token(String userId, List<String> role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public Token setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public List<String> getRole() {
        return role;
    }

    public Token setRole(List<String> role) {
        this.role = role;
        return this;
    }
}
