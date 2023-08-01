package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import com.bloomboard.promptboard.security.repository.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    //private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    public void save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);

        userRepository.save(user);
    }

    public User findByUsernameIgnoreCase(String username) {
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);
        return optionalUser.orElseThrow(() -> new NoResultException(
                String.format("Could not find user %s.", username)
        ));
    }

}
