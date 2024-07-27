package com.bloomboard.promptboard.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotEmpty
    @Size(min=5, max=20) //At least 5 characters long
    private String username;
    @NotEmpty
    @Size(min=8, max=32) //At least 8 characters long
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
