package com.restproject.restservice.security.service;

public interface ISecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
