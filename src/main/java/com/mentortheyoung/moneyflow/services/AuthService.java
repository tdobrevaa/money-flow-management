package com.mentortheyoung.moneyflow.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public AuthService(AuthenticationManager authManager, JWTService jWTService) {
        this.authManager = authManager;
        this.jwtService = jWTService;
    }

    public String verify(String username, String password) {
      Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      if (authentication.isAuthenticated())
        return jwtService.generateToken(username);

    return "Failed";
    }
}
