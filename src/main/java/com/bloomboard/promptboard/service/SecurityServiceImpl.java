package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.User;
import com.bloomboard.promptboard.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private Authentication getAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
            throw new AuthenticationCredentialsNotFoundException("Authentication Credentials Not Found");
        }
        return authentication;
    }
    @Override
    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    @Override
    public String getAuthenticatedUsername() {
        return getAuthentication().getName();
    }

    @Override
    public UserDetails getAuthenticatedUser() {
        Authentication authentication = getAuthentication();
        return (UserDetails) authentication.getPrincipal();
                //userDetailsService.loadUserByUsername(authentication.getName());
    }

    //TODO: Delete later
    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = new User("dummyUsername","dummy@email.com", "dummyPassword");
                //userDetailsService.loadUserByUsername(username);
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
