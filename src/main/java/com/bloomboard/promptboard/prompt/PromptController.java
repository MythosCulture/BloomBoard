package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
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

    @PostMapping("/edit")
    public String updatePrompt (@ModelAttribute("updatePromptForm") @Valid PromptRequest promptRequest, BindingResult bindingResult) {
        promptService.updatePrompt(promptRequest, securityService.getAuthenticatedUsername());

        return "redirect:/home";
    }

    @GetMapping("/all")
    public String viewSearch_AllPrompts(Model model) {
        model.addAttribute("prompts", promptService.findAllPrompts());
        return "searchView";
    }

    @GetMapping("/myprompts/all")
    public String viewSearch_MyPrompts(Model model) {
        model.addAttribute("prompts", promptService.findByOwner(securityService.getAuthenticatedUsername()));

        return "searchView";
    }

    @GetMapping("/{tag}") //TODO: test/implement
    public List<Prompt> getPromptsByTag(@PathVariable String tag) {
       return promptService.findByTag(tag);
    }

    //Todo: Search by phrase (search tag, name, and content)
    //Todo: update prompt
    //Todo: get tags

}
