package com.bloomboard.promptboard.controller;

import com.bloomboard.promptboard.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private final SecurityService securityService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @GetMapping({"","/options"})
    public String viewAccount (Model model) {
        return "accountView";
    }
}
