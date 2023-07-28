package com.bloomboard.promptboard;

import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping({"","/options"})
    public String viewAccount (Model model) {
        return "accountView";
    }
}
