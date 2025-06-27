package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.SearchRequest;
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
import java.util.ArrayList;
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

    public List<Prompt> findByUser_id (Long userId) {
        return promptRepository.findByUserId(userId);
    }

    public List<Prompt> findAllPrompts(){
        List<Prompt> prompts = promptRepository.findAll();
        formatSummaries(prompts);
        return prompts;
    }

    public Prompt getPromptById(Long id){
        Optional<Prompt> optionalPrompt = promptRepository.findById(id);
        return optionalPrompt.orElseThrow(() -> new NoResultException(
                String.format("Could not find prompt with id: %s.", id)
        ));
    }

    public Prompt createPrompt (PromptRequest prompt, Long userId) {
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

        Prompt savedPrompt = promptRepository.save(newPrompt);
        logMessage(newPrompt,String.format("created by user[%d]", userId));
        return savedPrompt;
    }

    public Prompt updatePrompt (Long promptId, PromptRequest promptRequest, Long userId) {
        Prompt prompt = getPromptById(promptId);
        if (!prompt.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to edit this prompt.");
        }

        prompt.setTitle(promptRequest.getTitle());
        prompt.setContent(promptRequest.getContent());
        prompt.setSummary(promptRequest.getSummary());
        prompt.setLastModified(OffsetDateTime.parse(promptRequest.getSubmissionDate()));

        String[] promptTags = tagService.getFormattedTagsString(promptRequest.getTags());
        Set<Tag> tagSet = tagService.saveNewTags(promptTags);
        prompt.setTags(tagSet);

        Prompt updatedPrompt = promptRepository.save(prompt);
        logMessage(prompt,String.format("updated by user[%d]", userId));
        return updatedPrompt;
    }
    public void deletePrompt(Long promptId, Long userId) {
        Prompt deletePrompt = getPromptById(promptId);
        if (!deletePrompt.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this prompt.");
        }

        promptRepository.delete(deletePrompt);
        logMessage(deletePrompt, String.format("deleted by user[%d]", userId));
    }

    public void deletePromptsByUser(Long userId){
        List<Prompt> promptsToDelete = findByUser_id(userId);
        if (promptsToDelete.isEmpty()) {
            throw new NoResultException("No prompts found for user with ID " + userId);
        }
        for (Prompt prompt: promptsToDelete) {
                promptRepository.delete(prompt);
                logMessage(prompt, String.format("Mass deletion by userId: user[%d]", userId));
        }
    }

    private List<Prompt> formatSummaries(List<Prompt> promptList) {
        for (Prompt prompt : promptList) {
            if (prompt.getSummary() == null || prompt.getSummary().equals("")) {
                String summary = prompt.getContent().length() < 252 ? prompt.getContent() : prompt.getContent().substring(0,252) + "...";
                prompt.setSummary(summary);
            }
        }
        return promptList;
    }
    public List<Prompt> searchByPhrase(String phrase) {
        return promptRepository.findByPhrase(phrase.trim().toLowerCase());
    }
    public List<Prompt> searchByTags(List<String> tags) {
        //Set all tags to lowercase before searching to prevent case mismatching
        tags.replaceAll(String::toLowerCase);
        return promptRepository.findByTagsIn(tags);
    }
    public List<Prompt> searchPrompts(SearchRequest searchRequest) {
        List<Prompt> searchedPrompts = new ArrayList<>();

        // Search by phrase if it's provided
        List<Prompt> byPhrase = new ArrayList<>();
        if (searchRequest.getPhrase() != null && !searchRequest.getPhrase().isEmpty()) {
            byPhrase = searchByPhrase(searchRequest.getPhrase());
        }

        // Search by tags if provided
        List<Prompt> byTags = new ArrayList<>();
        if (searchRequest.getTags() != null && !searchRequest.getTags().isEmpty()) {
            byTags = searchByTags(searchRequest.getTags());
        }

        // Combine both lists
        if (byPhrase.isEmpty() && byTags.isEmpty()) {
            // If both searches return empty, return empty list
            return searchedPrompts;
        } else if (byPhrase.isEmpty()) {
            searchedPrompts = byTags; // Only tags found
        } else if (byTags.isEmpty()) {
            searchedPrompts = byPhrase; // Only phrase found
        } else {
            // If both lists have results, only return the intersection
            for (Prompt prompt : byPhrase) {
                if (byTags.contains(prompt)) {
                    searchedPrompts.add(prompt);
                }
            }
        }

        formatSummaries(searchedPrompts);
        return searchedPrompts;
    }

    private void logMessage(Prompt prompt, String msg){
        String logMessage = String.format("Prompt[%d]: %s | %s",
                prompt.getId(), msg, prompt.toString());
        logger.info(logMessage);
    }
}
