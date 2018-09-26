package com.example.zuulgateway.security.provider;

import com.example.zuulgateway.security.filter.CustomSimpleToken;
import com.example.zuulgateway.security.token.CustomBasicToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class CustomSimpleAuthenticationProvider implements AuthenticationProvider {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomSimpleAuthenticationProvider(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getPrincipal().toString();
        UserDetails user = userDetailsManager.loadUserByUsername(userId);
        if (user != null) {
            CustomSimpleToken authResult =  new CustomSimpleToken(userId, user.getAuthorities());
            authResult.setDetails(user.getUsername());

            return authResult;
        }
        throw new BadCredentialsException("Invalid token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomSimpleToken.class.isAssignableFrom(authentication);
    }
}
