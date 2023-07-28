package com.bloomboard.promptboard.security.model;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterRequest{

    @NotEmpty
    @Size(min=5, max=20) //At least 5 characters long
    private String username;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min=8, max=32) //At least 8 characters long
    private String password;
    @NotEmpty
    @Size(min=8, max=32) //At least 8 characters long
    private String passwordConfirm;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() { return passwordConfirm; }

    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    @Override
    public String toString() {
        return "RegisterRequestModel [name=" + username + ", email=" + email + ", password=" + passwordConfirm + "]";
    }
}

