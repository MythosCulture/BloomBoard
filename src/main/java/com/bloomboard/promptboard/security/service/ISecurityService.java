package com.bloomboard.promptboard.security.service;

public interface ISecurityService {
    boolean isAuthenticated();
    String getAuthenticatedUsername();
    UserDetails getAuthenticatedUser();
    AuthenticationResponse authenticate(LoginRequest request);
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse generateJwtToken(String username);
    void autoLogin(String username, String password);
}
