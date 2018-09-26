package com.example.zuulgateway.security.filter;

import com.example.zuulgateway.security.token.CustomBasicToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
