package com.example.zuulgateway.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;

public class CustomBasicTokenFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public CustomBasicTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.toLowerCase().startsWith("basic ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(6);
        token = URLDecoder.decode(new String(Base64.getDecoder().decode(token), "utf-8"), "utf-8");
        String username = token.split(":")[0];
        String password = token.split(":")[1];

        Authentication requestToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticateResult = authenticationManager.authenticate(requestToken);

        SecurityContextHolder.getContext().setAuthentication(authenticateResult);
        filterChain.doFilter(request, response);
    }
}
