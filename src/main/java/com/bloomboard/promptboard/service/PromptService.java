package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.Prompt;
import com.bloomboard.promptboard.model.PromptRequest;
import com.bloomboard.promptboard.model.SearchRequest;

import java.util.List;

public interface PromptService {
    Prompt getPromptById(Long id);
    List<Prompt> findAllPrompts();
    Prompt createPrompt (PromptRequest prompt, Long userId);
    Prompt updatePrompt (Long promptId, PromptRequest prompt, Long userId);
    void deletePrompt(Long promptId, Long userId);
    void deletePromptsByUser(Long userId);
    List<Prompt> findByUser_id (Long userId);
    List<Prompt> searchByPhrase(String phrase);
    List<Prompt> searchByTags(List<String> tags);
    List<Prompt> searchPrompts(SearchRequest searchRequest);
    //void logMessage(Prompt prompt, String msg);
}
