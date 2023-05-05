package com.bloomboard.promptboard;

import com.bloomboard.promptboard.prompt.PromptRequest;
import com.bloomboard.promptboard.prompt.PromptService;
import com.bloomboard.promptboard.security.model.RegisterRequest;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

@Controller //@RestController //doesnt work with thymeleaf
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private PromptService promptService;

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

        User newUser = new User(registerRequest.getUsername(),registerRequest.getEmail(),registerRequest.getPassword());

        if (bindingResult.hasErrors()) {
            logger.info(bindingResult.toString());
            return "registerView";
        }

        if(userService.findByUsernameIgnoreCase(newUser.getUsername()).isPresent()){ //Check for duplicate usernames
            //add fielderror to bindingresult so that error shows up on form
            FieldError error = new FieldError("userForm","username","An account already exists with that username.");
            bindingResult.addError(error);
            logger.info(bindingResult.toString());

            return "registerView";
        }

        userService.save(newUser);
        //securityService.autoLogin(newUser.getUsername(), newUser.getPassword()); //TODO: autologin doesn't work
        //securityService.autoLogin(newUser.getUsername(), newUser.getPasswordConfirm()); //TODO: add .getPasswordConfirm()

        return "redirect:/home"; //TODO: Make /home page
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        } else {
            if (error != null)
                model.addAttribute("error", "Your username and password is invalid.");
            if (logout != null)
                model.addAttribute("message", "You have been logged out successfully.");

            return "loginView";
        }
    }

    @GetMapping({"/","/home"})
    public String viewWelcome(Model model) {
        //List<Prompt> newPrompt = promptService.findByOwner(securityService.getAuthenticatedUsername());
        model.addAttribute("prompts", promptService.findByOwner(securityService.getAuthenticatedUsername()));
        model.addAttribute("updatePromptForm", new PromptRequest());

        return "homeView";
    }
}
