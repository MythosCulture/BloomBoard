package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.AuthenticationResponse;
import com.bloomboard.promptboard.model.LoginRequest;
import com.bloomboard.promptboard.model.RegisterRequest;
import com.bloomboard.promptboard.model.User;
import com.bloomboard.promptboard.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            return false;
        }

        return authentication.isAuthenticated();
    }

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            throw new AuthenticationCredentialsNotFoundException("Authentication Credentials Not Found");
        }

        return authentication.getName();
    }

    public UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            throw new AuthenticationCredentialsNotFoundException("Authentication Credentials Not Found");
        }
        return userDetailsService.loadUserByUsername(authentication.getName());
    }

    public AuthenticationResponse authenticate(LoginRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                request.getPassword(),
                userDetails.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

            logger.info("User logged in successfully: " + request.getUsername());

            String jwtToken = JwtUtil.generateToken(request.getUsername());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else throw new BadCredentialsException("Authentication failed: invalid credentials provided.");
    }

    public AuthenticationResponse register(RegisterRequest request){
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPasswordConfirm())
        );
        userDetailsManager.createUser(user);

        String jwtToken = JwtUtil.generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        if(authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info(String.format("Auto login %s successfully!", username)); //TODO: better logging
            logger.debug(String.format("Auto login %s successfully!", username));
        }
    }

}
