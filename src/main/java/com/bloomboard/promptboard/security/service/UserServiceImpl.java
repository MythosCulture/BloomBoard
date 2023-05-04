package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import com.bloomboard.promptboard.security.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private final IUserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    //private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    @Override
    public void save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);

        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

}
