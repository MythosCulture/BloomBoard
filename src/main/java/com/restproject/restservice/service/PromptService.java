package com.restproject.restservice.service;

import com.restproject.restservice.domain.Prompt;
import com.restproject.restservice.repository.IPromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromptService {

    @Autowired
    private IPromptRepository promptRepository;

    public List<Prompt> findAllPrompts(){
        return promptRepository.findAll();
    }

    public Prompt getPromptById(long id){
        Optional<Prompt> optionalPrompt = promptRepository.findById(id);

        return optionalPrompt.orElseThrow();
    }

    public Prompt createPrompt (Prompt prompt) {
        Prompt newPrompt = new Prompt(prompt.getName(), prompt.getContent(), prompt.getTags());
        return promptRepository.save(newPrompt);
    }

    public Prompt updatePrompt (Prompt prompt) {
        Prompt updatedPrompt = getPromptById(prompt.getId());
        updatedPrompt.setName(prompt.getName());
        updatedPrompt.setContent(prompt.getContent());
        updatedPrompt.setTags(prompt.getTags());

        return promptRepository.save(updatedPrompt);
    }

    public List<Prompt> findByTag(String tag) {
        return promptRepository.findByTagsContainingIgnoreCase(" " + tag +",");
    }
    //TODO: Add searchPhrase method
    //public List<Prompt> searchPhrase(String phrase) {}
}
