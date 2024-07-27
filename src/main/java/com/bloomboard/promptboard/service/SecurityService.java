package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.AuthenticationResponse;
import com.bloomboard.promptboard.model.LoginRequest;
import com.bloomboard.promptboard.model.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {
    boolean isAuthenticated();
    String getAuthenticatedUsername();
    UserDetails getAuthenticatedUser();
    AuthenticationResponse authenticate(LoginRequest request);
    AuthenticationResponse register(RegisterRequest request);
    void autoLogin(String username, String password);
}
