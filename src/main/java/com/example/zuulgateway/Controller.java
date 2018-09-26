package com.example.zuulgateway;

import com.example.zuulgateway.security.token.CustomBasicToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;

@RestController
public class Controller {

    @GetMapping("/tokens")
    public ResponseEntity token(Principal principal) throws UnsupportedEncodingException {
        String token = Base64.getEncoder().encodeToString(principal.getName().getBytes("utf-8"));
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }
}
