package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;

import java.util.List;

public interface IPromptService {
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
