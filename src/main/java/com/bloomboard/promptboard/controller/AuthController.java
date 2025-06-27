package com.bloomboard.promptboard.controller;

import com.bloomboard.promptboard.model.AuthenticationResponse;
import com.bloomboard.promptboard.model.LoginRequest;
import com.bloomboard.promptboard.model.RegisterRequest;
import com.bloomboard.promptboard.security.JwtUtil;
import com.bloomboard.promptboard.service.AuthService;
import com.bloomboard.promptboard.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public ResponseEntity<Object> login() {
        if (securityService.isAuthenticated()) {
            // Return a 302 Found status with the redirect location
            return ResponseEntity.status(302).header("Location", "/").build();
        }
        // Return the login form as JSON with a 200 OK status
        LoginRequest loginForm = new LoginRequest();
        return ResponseEntity.ok(loginForm);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        try{
            if (securityService.isAuthenticated()) {
                String token = JwtUtil.generateToken(securityService.getAuthenticatedUsername());
                return ResponseEntity.ok(new AuthenticationResponse(token));
            }
            return ResponseEntity.ok(authService.authenticate(request));
        } catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/register")
    public ResponseEntity<Object> register() {
        if (securityService.isAuthenticated()) {
            // Return a 302 Found status with the redirect location
            return ResponseEntity.status(302).header("Location", "/").build();
        }
        // Return the user form as JSON with a 200 OK status
        RegisterRequest registerForm = new RegisterRequest();
        return ResponseEntity.ok(registerForm);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthenticationResponse(e.getMessage())
            );
        }
    }

}
