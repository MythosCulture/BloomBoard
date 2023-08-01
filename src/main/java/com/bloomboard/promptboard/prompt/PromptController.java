package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.SecurityServiceImpl;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import com.bloomboard.promptboard.tag.TagService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/prompts")
public class PromptController {

    Logger logger = LoggerFactory.getLogger(PromptController.class);
    @Autowired
    private final PromptService promptService;
    @Autowired
    private final TagService tagService;
    @Autowired
    private final SecurityServiceImpl securityService;
    @Autowired
    private final UserServiceImpl userService;


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

    @GetMapping("/browse")
    public String viewBrowse(Model model) {
        //I don't know why but this needs to be called first for the readMore modal to not crash the page...
        User user = userService.findByUsernameIgnoreCase(securityService.getAuthenticatedUsername());
        //get all prompts first

        //TODO: Replace default search / add pagination
        List<Prompt> allPrompts = promptService.findAllPrompts(); //this is default search

        //also used in /home for my prompts & /browse for the browse method
        //set summary to 252 characters of content + "..." if summary is empty
        for (Prompt prompt : allPrompts) {
            if (prompt.getSummary() == null || prompt.getSummary().equals("")) {
                String summary = prompt.getContent().length() < 252 ? prompt.getContent() : prompt.getContent().substring(0,252) + "...";
                prompt.setSummary(summary);
            }
        }

        model.addAttribute("prompts", allPrompts);
        model.addAttribute("updatePromptForm", new PromptRequest());
        model.addAttribute("deletePromptForm", new PromptRequest());
        model.addAttribute("searchPromptForm", new SearchRequest());
        return "searchView";
    }

    @PostMapping("/browse")
    public String browse(@ModelAttribute ("searchPromptForm") @Valid SearchRequest searchRequest, BindingResult bindingResult, Model model) {
        //TODO: Dont know how to set up this post mapping when theres other model attributes
        //TODO: Fix the filter bar taking up so much room...
        User user = userService.findByUsernameIgnoreCase(securityService.getAuthenticatedUsername());

        List<Prompt> searchedPrompts = new ArrayList<Prompt>();

        //search through database using a phrase
        List<Prompt> ByPhrase = new ArrayList<Prompt>();
        if (searchRequest.getPhrase() != null && !searchRequest.getPhrase().isEmpty()) {
            ByPhrase = promptService.searchByPhrase(searchRequest.getPhrase());
        }

        //search through database using multiple tags
        List<Prompt> ByTags = new ArrayList<Prompt>();
        if (searchRequest.getTags() != null && !searchRequest.getTags().isEmpty()) {
            ByTags = promptService.searchByTags(searchRequest.getTags());
        }

        //Combine both lists
        if (ByPhrase.isEmpty() && ByTags.isEmpty()) {
            //Both searches didn't turn up results
        } else if (ByPhrase.isEmpty()) {
            searchedPrompts = ByTags;
        } else if (ByTags.isEmpty()) {
            searchedPrompts = ByPhrase;
        } else {
            for (Prompt prompt : ByPhrase) {
                if (ByTags.contains(prompt)) {
                    searchedPrompts.add(prompt);
                }
            }
        }

        //also used in /home for my prompts & /browse for the viewBrowse method
        //set summary to 252 characters of content + "..." if summary is empty
        for (Prompt prompt : searchedPrompts) {
            if (prompt.getSummary() == null || prompt.getSummary().equals("")) {
                String summary = prompt.getContent().length() < 252 ? prompt.getContent() : prompt.getContent().substring(0,252) + "...";
                prompt.setSummary(summary);
            }
        }

        //TODO: don't know if necessary? Do I need to re-add the model attributes? I probably need the first one for prompts
        //combine searches into one list then return
        model.addAttribute("prompts", searchedPrompts);
        model.addAttribute("updatePromptForm", new PromptRequest());
        model.addAttribute("deletePromptForm", new PromptRequest());
        model.addAttribute("searchPromptForm", searchRequest);
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
