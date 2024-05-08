package com.bloomboard.promptboard.security.service;

public interface ISecurityService {
    boolean isAuthenticated();
    String getAuthenticatedUsername();
    void autoLogin(String username, String password);
}
