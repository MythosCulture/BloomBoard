package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.AuthenticationResponse;
import com.bloomboard.promptboard.model.LoginRequest;
import com.bloomboard.promptboard.model.RegisterRequest;
import com.bloomboard.promptboard.model.User;
import com.bloomboard.promptboard.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class AuthServiceImpl implements AuthService{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserDetailsManager userDetailsManager;

    @Override
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
    @Override
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
}
