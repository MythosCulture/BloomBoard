package com.restproject.restservice.prompt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@RestController //doesnt work with thymeleaf
@Controller
public class PromptController {

    @Autowired
    private PromptService promptService;

    @GetMapping("/prompt/all")
    public List<Prompt> getAllPrompts() { return promptService.findAllPrompts(); }

    //Todo: get prompt id
    //Todo: create prompt
    //Todo: update prompt
    //Todo: get tags

    @GetMapping("/prompt/{tag}")
    public List<Prompt> getPromptsByTag(@PathVariable String tag) {
       return promptService.findByTag(tag);
    }

    //Todo: Search by phrase (search tag, name, and content)

}
