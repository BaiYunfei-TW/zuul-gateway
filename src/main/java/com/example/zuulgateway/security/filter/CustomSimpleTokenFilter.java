package com.example.zuulgateway.security.filter;

import com.example.zuulgateway.security.token.CustomBasicToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;

public class CustomSimpleTokenFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    public CustomSimpleTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.toLowerCase().startsWith("simple ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        token = URLDecoder.decode(new String(Base64.getDecoder().decode(token), "utf-8"), "utf-8");

        Authentication requestToken = new CustomSimpleToken(token);
        Authentication authenticateResult = null;
        try {
            authenticateResult = authenticationManager.authenticate(requestToken);
        } catch (Exception e) {
            e.printStackTrace();
            filterChain.doFilter(request, response);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticateResult);
        filterChain.doFilter(request, response);
    }
}
