package com.bloomboard.promptboard;

import com.bloomboard.promptboard.security.service.ISecurityService;
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
    private final ISecurityService securityService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @GetMapping({"","/options"})
    public String viewAccount (Model model) {
        return "accountView";
    }
}
