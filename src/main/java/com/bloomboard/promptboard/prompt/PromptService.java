package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.Tag;
import com.bloomboard.promptboard.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PromptService {

    @Autowired
    private IPromptRepository promptRepository;
    @Autowired
    private TagService tagService;

    public List<Prompt> findAllPrompts(){
        return promptRepository.findAll();
    }

    public Prompt getPromptById(long id){
        Optional<Prompt> optionalPrompt = promptRepository.findById(id);
        return optionalPrompt.orElseThrow(() -> new NoResultException(
                String.format("Could not find prompt with id: %s.", id)
        ));
    }

    public void createPrompt (PromptRequest prompt, User user) {
        String[] promptTags = tagService.getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);

        Prompt newPrompt = new Prompt(
                prompt.getTitle(),
                prompt.getSummary(),
                prompt.getContent(),
                tagSet,
                user
        );

        promptRepository.save(newPrompt);
    }

    public void updatePrompt (PromptRequest prompt, User user) {
        Prompt updatedPrompt = getPromptById(prompt.getId());
        if (!updatedPrompt.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to edit this prompt.");
        }

        updatedPrompt.setTitle(prompt.getTitle());
        updatedPrompt.setContent(prompt.getContent());
        updatedPrompt.setSummary(prompt.getSummary());

        String[] promptTags = tagService.getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);
        updatedPrompt.setTags(tagSet);

        promptRepository.save(updatedPrompt);
    }

    /* //TODO: implement tag search on prompts
    public List<Prompt> findByTag(String tag) {
        return promptRepository.findByTagsContainingIgnoreCase(tag);
    }
     */
    public List<Prompt> findByUser_id (long user_id) {
        return promptRepository.findByUser_id(user_id);
    }

    public void deletePrompt(long id) {
        Prompt prompt = getPromptById(id);
        promptRepository.delete(prompt);
    }

    //TODO: Add searchPhrase method
    //public List<Prompt> searchPhrase(String phrase) {}
}
