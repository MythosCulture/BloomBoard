package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import com.bloomboard.promptboard.tag.TagService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/prompts")
public class PromptController {

    Logger logger = LoggerFactory.getLogger(PromptController.class);
    @Autowired
    private PromptService promptService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/new") //GET
    public String createPrompt (Model model) {
        model.addAttribute("createPromptForm", new PromptRequest());
        return "createPromptView";
    }
    @PostMapping("/new")
    public String createPrompt(@ModelAttribute("createPromptForm") @Valid PromptRequest promptRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info(bindingResult.toString());
            return "createPromptView";
        }

        User user = userService.findByUsernameIgnoreCase(
                securityService.getAuthenticatedUsername()
        );

        promptService.createPrompt(promptRequest, user);

        return "redirect:/home";
    }

    @PostMapping("/edit")
    public String updatePrompt (@ModelAttribute("updatePromptForm") @Valid PromptRequest promptRequest, BindingResult bindingResult) {
        User user = userService.findByUsernameIgnoreCase(
                securityService.getAuthenticatedUsername()
        );

        promptService.updatePrompt(promptRequest, user);

        return "redirect:/home";
    }
    @PostMapping("/delete")
    public String deletePrompt (@ModelAttribute("updatePromptForm") @Valid PromptRequest promptRequest, BindingResult bindingResult) {
        User user = userService.findByUsernameIgnoreCase(
                securityService.getAuthenticatedUsername()
        );

        promptService.deletePrompt(promptRequest.getId());

        return "redirect:/home";
    }

    @GetMapping("/all")
    public String viewSearch_AllPrompts(Model model) {
        model.addAttribute("prompts", promptService.findAllPrompts());
        return "searchView";
    }

    @GetMapping("/myprompts/all")
    public String viewSearch_MyPrompts(Model model) {
        User user = userService.findByUsernameIgnoreCase(securityService.getAuthenticatedUsername());
        model.addAttribute(
                "prompts",
                promptService.findByUser_id(user.getId())
        );

        return "searchView";
    }

    //Searching by tags//
    /*
    @GetMapping("/{tag}") //TODO: implement tag search on prompts
    public List<Prompt> getPromptsByTag(@PathVariable String tag) {
       return promptService.findByTag(tag);
    }
     */
}
