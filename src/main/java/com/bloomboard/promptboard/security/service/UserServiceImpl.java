package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import com.bloomboard.promptboard.security.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final ISecurityService securityService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public List<User> findByUserRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

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
        if(!userExists(user.getUsername())) {
            userRepository.save((User)user);
        } else {
            throw new IllegalArgumentException(
                    String.format("UserDetailsManager error: Username [%s] already exists", user.getUsername())
            );
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        if(!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(
                    String.format("UserDetailsManager error: Username [%s] not found", user.getUsername())
            );
        } else {
            userRepository.save((User)user);
        }
    }

    @Override
    public void deleteUser(String username) {
        if(!userExists(username)) {
            throw new UsernameNotFoundException(
                    String.format("UserDetailsManager error: Username [%s] not found", username)
            );
        } else {
            userRepository.deleteByUsernameIgnoreCase(username);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = securityService.getAuthenticatedUsername();
        if (username == null) {
            throw new UsernameNotFoundException("UserDetailsManager error: No authenticated user found");
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    oldPassword
            );
            authenticationManager.authenticate(authenticationToken);

            if (authenticationToken.isAuthenticated()) {
                User user = (User)loadUserByUsername(username);
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Old password is incorrect");
            }
        } catch (Exception e) {
            logger.error("Password change error: ", e);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isPresent();
    }
}
