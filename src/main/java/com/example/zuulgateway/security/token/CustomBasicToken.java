package com.example.zuulgateway.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomBasicToken extends UsernamePasswordAuthenticationToken {
    public CustomBasicToken(String username, String password) {
        super(username, password);
    }

    public CustomBasicToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username,null, authorities);
    }
}
