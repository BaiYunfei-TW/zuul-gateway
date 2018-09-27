package com.example.zuulgateway.security.token;

import java.util.List;

public class Token {
    private String usserId;
    private List<String> role;

    public Token() {
    }

    public Token(String usserId, List<String> role) {
        this.usserId = usserId;
        this.role = role;
    }

    public String getUsserId() {
        return usserId;
    }

    public Token setUsserId(String usserId) {
        this.usserId = usserId;
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
