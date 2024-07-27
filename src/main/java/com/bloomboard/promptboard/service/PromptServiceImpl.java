package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.repository.IPromptRepository;
import com.bloomboard.promptboard.model.Prompt;
import com.bloomboard.promptboard.model.PromptRequest;
import com.bloomboard.promptboard.model.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {

    private static final Logger logger = LoggerFactory.getLogger(PromptServiceImpl.class);
    @Autowired
    private final IPromptRepository promptRepository;
    @Autowired
    private final TagService tagService;

    public List<Prompt> findAllPrompts(){
        return promptRepository.findAll();
    }

    public Prompt getPromptById(Long id){
        Optional<Prompt> optionalPrompt = promptRepository.findById(id);
        return optionalPrompt.orElseThrow(() -> new NoResultException(
                String.format("Could not find prompt with id: %s.", id)
        ));
    }

    public void createPrompt (PromptRequest prompt, Long userId) {
        String[] promptTags = tagService.getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);

        Prompt newPrompt = new Prompt(
                prompt.getTitle(),
                prompt.getSummary(),
                prompt.getContent(),
                tagSet,
                userId,
                OffsetDateTime.parse(prompt.getSubmissionDate())
        );

        promptRepository.save(newPrompt);
        logMessage(newPrompt,String.format("created by user[%d]", userId));
    }

    public void updatePrompt (PromptRequest prompt, Long userId) {
        Prompt updatedPrompt = getPromptById(prompt.getId());
        if (!updatedPrompt.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to edit this prompt.");
        }

        updatedPrompt.setTitle(prompt.getTitle());
        updatedPrompt.setContent(prompt.getContent());
        updatedPrompt.setSummary(prompt.getSummary());
        updatedPrompt.setLastModified(OffsetDateTime.parse(prompt.getSubmissionDate()));

        String[] promptTags = tagService.getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);
        updatedPrompt.setTags(tagSet);

        promptRepository.save(updatedPrompt);
        logMessage(updatedPrompt,String.format("updated by user[%d]", userId));
    }

    public List<Prompt> findByUser_id (Long userId) {
        return promptRepository.findByUserId(userId);
    }

    public void deletePrompt(Long promptId, Long userId) {
        Prompt deletePrompt = getPromptById(promptId);
        if (!deletePrompt.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this prompt.");
        }

        promptRepository.delete(deletePrompt);
        logMessage(deletePrompt, String.format("deleted by user[%d]", userId));
    }

    //for user mass deleting prompts
    //TODO: make another method just for utility?
    public void deletePromptsByUser(List<Prompt> promptsToDelete, Long userId){
        for (Prompt prompt: promptsToDelete) {
            if (prompt.getUserId() != userId) {
                logMessage(prompt,
                        String.format("prompt was not deleted because user[%d] is not the owner", userId)
                );
            } else {
                promptRepository.delete(prompt);
                logMessage(prompt, String.format("deleted by user[%d]", userId));
            }
        }
    }

    public List<Prompt> searchByPhrase(String phrase) {
        return promptRepository.findByPhrase(phrase.trim().toLowerCase());
    }
    public List<Prompt> searchByTags(List<String> tags) {
        //Set all tags to lowercase before searching to prevent case mismatching
        tags.replaceAll(String::toLowerCase);
        return promptRepository.findByTagsIn(tags);
    }

    private void logMessage(Prompt prompt, String msg){
        String logMessage = String.format("Prompt[%d]: %s | %s",
                prompt.getId(), msg, prompt.toString());
        logger.info(logMessage);
    }
}
