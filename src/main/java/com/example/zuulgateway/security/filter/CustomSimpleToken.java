package com.example.zuulgateway.security.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomSimpleToken extends AbstractAuthenticationToken {

    private final String userId;

    public CustomSimpleToken(String userId) {
        this(userId, null);
    }

    public CustomSimpleToken(String userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.userId;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }
}
