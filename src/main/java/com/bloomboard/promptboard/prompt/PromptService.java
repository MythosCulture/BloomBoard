package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.Tag;
import com.bloomboard.promptboard.tag.TagService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PromptService {

    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);
    @Autowired
    private final IPromptRepository promptRepository;
    @Autowired
    private final TagService tagService;

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
                user,
                prompt.getSubmissionDate()
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
        updatedPrompt.setLastModified(prompt.getSubmissionDate());

        String[] promptTags = tagService.getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);
        updatedPrompt.setTags(tagSet);

        promptRepository.save(updatedPrompt);
    }

    public List<Prompt> findByUser_id (long user_id) {
        return promptRepository.findByUser_id(user_id);
    }

    public void deletePrompt(long id) {
        Prompt prompt = getPromptById(id);
        promptRepository.delete(prompt);
    }

    public List<Prompt> searchByPhrase(String phrase) {
        return promptRepository.findByPhrase(phrase.trim().toLowerCase());
    }
    public List<Prompt> searchByTags(List<String> tags) {
        //Set all tags to lowercase before searching to prevent case mismatching
        tags.replaceAll(String::toLowerCase);
        return promptRepository.findByTagsIn(tags);
    }
}
