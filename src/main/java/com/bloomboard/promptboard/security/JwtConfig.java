package com.bloomboard.promptboard.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secretkey;

    @PostConstruct
    public void init(){
        JwtUtil.setSecretKey(secretkey);
    }
}
