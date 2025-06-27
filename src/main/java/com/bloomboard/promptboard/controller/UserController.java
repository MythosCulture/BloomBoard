package com.bloomboard.promptboard.controller;

import com.bloomboard.promptboard.service.PromptService;
import com.bloomboard.promptboard.model.Prompt;
import com.bloomboard.promptboard.model.PromptRequest;
import com.bloomboard.promptboard.model.User;
import com.bloomboard.promptboard.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller //@RestController //doesnt work with thymeleaf
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final UserDetailsService userDetailsService;
    @Autowired
    private final SecurityService securityService;
    @Autowired
    private final PromptService promptService;

    //update user (@PUT), delete user, get user

    //@GetMapping({"/","/home"})
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
