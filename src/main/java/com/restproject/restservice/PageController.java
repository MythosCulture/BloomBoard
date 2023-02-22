package com.restproject.restservice;

import com.restproject.restservice.prompt.PromptController;
import com.restproject.restservice.prompt.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private PromptService promptService;

    //login
    @GetMapping("/login")
    public String viewLogin (Model model) {
        return "loginView";
    }

    //home
    @GetMapping("/")
    public String viewDashboard(Model model) {
        return "Hello World!";
    }

    //search prompts
    @GetMapping("/prompts")
    public String viewSearch(Model model) {
        model.addAttribute("prompts", promptService.findAllPrompts());
        return "searchView";
    }
}
