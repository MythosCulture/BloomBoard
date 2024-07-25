package com.bloomboard.promptboard;

import com.bloomboard.promptboard.prompt.IPromptService;
import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.PromptRequest;
import com.bloomboard.promptboard.security.model.AuthenticationResponse;
import com.bloomboard.promptboard.security.model.LoginRequest;
import com.bloomboard.promptboard.security.model.RegisterRequest;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.ISecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller //@RestController //doesnt work with thymeleaf
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final UserDetailsManager userDetailsManager;
    @Autowired
    private final UserDetailsService userDetailsService;
    @Autowired
    private final ISecurityService securityService;
    @Autowired
    private final IPromptService promptService;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register") //GET
    public String register (Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new RegisterRequest());

        return "registerView";
    }
    @PostMapping("/register") //POST
    public String register (@ModelAttribute("userForm") @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (!Objects.equals(registerRequest.getPassword(), registerRequest.getPasswordConfirm())) {
            //adds fielderror to bindingresult so that error shows up on form
            FieldError passwordError = new FieldError("userForm", "passwordConfirm", "password doesn't match.");
            bindingResult.addError(passwordError);
        }

        //Form errors should be added before this line//
        if (bindingResult.hasErrors()) {
            return "registerView";
        }

        User newUser = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPasswordConfirm())
        );
        try {
            userDetailsService.loadUserByUsername(newUser.getUsername());
        } catch (UsernameNotFoundException e) {
            userDetailsManager.createUser(newUser);
            securityService.autoLogin(newUser.getUsername(), registerRequest.getPasswordConfirm());
            return "redirect:/home";
        }
        FieldError error = new FieldError("userForm","username","An account already exists with that username.");
        bindingResult.addError(error);

        logger.info(bindingResult.toString());
        return "registerView";

    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            logger.info("User Login Authenticated: " + securityService.getAuthenticatedUsername());
            return "redirect:/";
        } else {
            if (error != null)
                model.addAttribute("error", "Your username and password is invalid.");
            if (logout != null)
                model.addAttribute("message", "You have been logged out successfully.");

            return "loginView";
        }
    }

    @GetMapping("/api/register")
    public ResponseEntity<Object> register() {
        if (securityService.isAuthenticated()) {
            // Return a 302 Found status with the redirect location
            return ResponseEntity.status(302).header("Location", "/").build();
        }
        // Return the user form as JSON with a 200 OK status
        RegisterRequest registerForm = new RegisterRequest();
        return ResponseEntity.ok(registerForm);
    }

    @PostMapping("/api/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(securityService.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthenticationResponse(e.getMessage())
            );
        }
    }
    @GetMapping("/api/login")
    public ResponseEntity<Object> login() {
        if (securityService.isAuthenticated()) {
            // Return a 302 Found status with the redirect location
            return ResponseEntity.status(302).header("Location", "/").build();
        }
        // Return the login form as JSON with a 200 OK status
        LoginRequest loginForm = new LoginRequest();
        return ResponseEntity.ok(loginForm);
    }
    @PostMapping("/api/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        try{
            if (securityService.isAuthenticated()) {
                return ResponseEntity.ok(securityService.generateJwtToken(request.getUsername()));
            }
            return ResponseEntity.ok(securityService.authenticate(request));
        } catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping({"/forgot-password"})
    public String viewForgotPassword(Model model) {
        // If logged in, redirect to home.
        // User should use account settings to reset password
        if (securityService.isAuthenticated()) {
            logger.info("User Login Authenticated: " + securityService.getAuthenticatedUsername() + "; redirecting from /forgot-password.");
            return "redirect:/"; //TODO: redirect to account settings
        }
        return "forgotPasswordView";
    }

    @GetMapping({"/","/home"})
    public String viewWelcome(Model model) {
        User user = (User) userDetailsService.loadUserByUsername(securityService.getAuthenticatedUsername());
        List<Prompt> userPrompts = promptService.findByUser_id(user.getId());

        //also used in /prompts/all
        //set summary to 252 characters of content + "..." if summary is empty
        //will update database if user edits prompt and saves again, this is intended function
        for (Prompt prompt : userPrompts) {
            if (prompt.getSummary() == null || prompt.getSummary().equals("")) {
                String summary = prompt.getContent().length() < 252 ? prompt.getContent() : prompt.getContent().substring(0,252) + "...";
                prompt.setSummary(summary);
            }
        }

        model.addAttribute("prompts", userPrompts);
        model.addAttribute("updatePromptForm", new PromptRequest());
        model.addAttribute("deletePromptForm", new PromptRequest());

        return "homeView";
    }
}
