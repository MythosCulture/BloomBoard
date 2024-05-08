package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import com.bloomboard.promptboard.security.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserDetailsManager {
    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> findByUserRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

    //TODO: Update the error to logger
    @Override
    public UserDetails loadUserByUsername(String username) {
        //Returns UserDetails but can be cast to User as needed
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(
                String.format("UserDetailsService error: Username [%s] not found", username)
        ));
    }

    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User)user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
