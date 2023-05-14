package com.bloomboard.promptboard.security.service;

public interface ISecurityService {
    boolean isAuthenticated();
    //TODO: add autologin
    void autoLogin(String username, String password);
}
