package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.security.model.User;

import java.util.Optional;

public interface IUserService {
    void save(User user);
    Optional<User> findByUsernameIgnoreCase(String username);

}
