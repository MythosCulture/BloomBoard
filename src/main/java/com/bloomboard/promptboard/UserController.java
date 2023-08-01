package com.bloomboard.promptboard;

import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.PromptRequest;
import com.bloomboard.promptboard.prompt.PromptService;
import com.bloomboard.promptboard.security.model.RegisterRequest;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.thymeleaf.util.StringUtils;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller //@RestController //doesnt work with thymeleaf
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final SecurityServiceImpl securityService;
    @Autowired
    private final PromptService promptService;

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
            logger.info(bindingResult.toString());
            return "registerView";
        }

        User newUser = new User(registerRequest.getUsername(),registerRequest.getEmail(),registerRequest.getPasswordConfirm());
        try {
            userService.findByUsernameIgnoreCase(newUser.getUsername());
        } catch (NoResultException e) {
            userService.save(newUser);
            //securityService.autoLogin(newUser.getUsername(), newUser.getPasswordConfirm()); //TODO: add autologin
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
        User user = userService.findByUsernameIgnoreCase(securityService.getAuthenticatedUsername());
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
