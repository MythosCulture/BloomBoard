package com.restproject.restservice.controller;

import com.restproject.restservice.domain.Prompt;
import com.restproject.restservice.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prompt")
public class PromptController {

    @Autowired
    private PromptService promptService;

    //Todo: get all prompts
    //Todo: get prompt id
    //Todo: create prompt
    //Todo: update prompt
    //Todo: get tags

    @GetMapping("/{tag}")
    public List<Prompt> getPromptsByTag(@PathVariable String tag) {
       return promptService.findByTag(tag);
    }

    //Todo: Search by phrase (search tag, name, and content)

}
