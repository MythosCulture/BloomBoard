package com.restproject.restservice.security.service;

import com.restproject.restservice.security.model.User;

import java.util.Optional;

public interface IUserService {
    void save(User user);
    Optional<User> findByUsernameIgnoreCase(String username);

}
