package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.AuthenticationResponse;
import com.bloomboard.promptboard.model.LoginRequest;
import com.bloomboard.promptboard.model.RegisterRequest;

public interface AuthService {
    AuthenticationResponse authenticate(LoginRequest request);
    AuthenticationResponse register(RegisterRequest request);
}
