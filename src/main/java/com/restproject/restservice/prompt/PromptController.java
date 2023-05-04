package com.restproject.restservice.prompt;

import com.restproject.restservice.security.model.RegisterRequest;
import com.restproject.restservice.security.service.SecurityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/prompts")
public class PromptController {

    Logger logger = LoggerFactory.getLogger(PromptController.class);
    @Autowired
    private PromptService promptService;
    @Autowired
    private SecurityServiceImpl securityService;


    @GetMapping("/")
    public String viewSearch_MyPrompts(Model model) {
        model.addAttribute("prompts", promptService.findByOwner(securityService.getAuthenticatedUsername()));
        return "searchView";
    }

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

        Prompt newPrompt = new Prompt(
                promptRequest.getTitle(),
                promptRequest.getContent(),
                promptRequest.getTags(),
                securityService.getAuthenticatedUsername()
        );

        promptService.createPrompt(newPrompt);

        return "redirect:/home";
    }

    @GetMapping("/all")
    public String viewSearch_AllPrompts(Model model) {
        model.addAttribute("prompts", promptService.findAllPrompts());
        return "searchView";
    }

    @GetMapping("/prompts/{tag}") //TODO: test/implement
    public List<Prompt> getPromptsByTag(@PathVariable String tag) {
       return promptService.findByTag(tag);
    }

    //Todo: Search by phrase (search tag, name, and content)
    //Todo: update prompt
    //Todo: get tags

}
