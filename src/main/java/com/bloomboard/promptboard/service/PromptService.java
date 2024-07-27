package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.Prompt;
import com.bloomboard.promptboard.model.PromptRequest;

import java.util.List;

public interface PromptService {
    Prompt getPromptById(Long id);
    List<Prompt> findAllPrompts();
    void createPrompt (PromptRequest prompt, Long userId);
    void updatePrompt (PromptRequest prompt, Long userId);
    void deletePrompt(Long promptId, Long userId);
    void deletePromptsByUser(List<Prompt> promptsToDelete, Long userId);
    List<Prompt> findByUser_id (Long userId);
    List<Prompt> searchByPhrase(String phrase);
    List<Prompt> searchByTags(List<String> tags);
    //void logMessage(Prompt prompt, String msg);
}
