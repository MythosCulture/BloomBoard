package com.bloomboard.promptboard.prompt;

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

        return optionalPrompt.orElseThrow(IllegalArgumentException::new);
    }

    public void createPrompt (Prompt prompt) {
        promptRepository.save(prompt);
    }

    public void updatePrompt (Prompt prompt) {
        Prompt updatedPrompt = getPromptById(prompt.getId());
        updatedPrompt.setTitle(prompt.getTitle());
        updatedPrompt.setContent(prompt.getContent());
        updatedPrompt.setTags(prompt.getTags());

        promptRepository.save(updatedPrompt);
    }

    public List<Prompt> findByTag(String tag) {
        return promptRepository.findByTagsContainingIgnoreCase(tag);
    }

    public List<Prompt> findByOwner(String owner) {
        return promptRepository.findByOwner(owner);
    }
    //TODO: Add searchPhrase method
    //public List<Prompt> searchPhrase(String phrase) {}
}
