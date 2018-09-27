package com.example.zuulgateway.filter;

import com.example.zuulgateway.security.token.Token;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AddTokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Token token = new Token(authentication.getName(),
                authentication.getAuthorities()
                        .stream()
                        .map(element -> ((GrantedAuthority) element).getAuthority())
                        .collect(Collectors.toList())
        );

        ctx.addZuulRequestHeader("x-authenticated-token", new Gson().toJson(token));
        return null;
    }
}
