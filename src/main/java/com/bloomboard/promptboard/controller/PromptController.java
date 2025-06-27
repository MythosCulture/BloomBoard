package com.bloomboard.promptboard.controller;

import com.bloomboard.promptboard.model.Prompt;
import com.bloomboard.promptboard.model.PromptRequest;
import com.bloomboard.promptboard.model.SearchRequest;
import com.bloomboard.promptboard.model.User;
import com.bloomboard.promptboard.service.PromptService;
import com.bloomboard.promptboard.service.SecurityService;
import com.bloomboard.promptboard.service.TagService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/prompts")
public class PromptController {

    private static final Logger logger = LoggerFactory.getLogger(PromptController.class);
    @Autowired
    private final PromptService promptService;
    @Autowired
    private final TagService tagService;
    @Autowired
    private final SecurityService securityService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @GetMapping("/create")
    public ResponseEntity<Object> createPrompt() {
        PromptRequest promptForm = new PromptRequest();
        return ResponseEntity.ok(promptForm);
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createPrompt(@RequestBody PromptRequest promptRequest) {
        try{
            User user = (User) securityService.getAuthenticatedUser();
            Prompt createdPrompt = promptService.createPrompt(promptRequest, user.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdPrompt);
        }  catch (Error e) { //TODO: InvalidPromptException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid prompt data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/update/{promptId}")
    public ResponseEntity<Object> updatePrompt(@PathVariable Long promptId, @RequestBody PromptRequest promptRequest) {
        try {
            User user = (User) securityService.getAuthenticatedUser();
            Prompt updatedPrompt = promptService.updatePrompt(promptId, promptRequest, user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedPrompt);
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the prompt");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Prompt>> getPromptsByUser(@PathVariable Long userId) {
        List<Prompt> prompts = promptService.findByUser_id(userId);
        if (prompts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(prompts);
        }
        return ResponseEntity.ok(prompts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prompt>> getAllPrompts() {
        List<Prompt> prompts = promptService.findAllPrompts();
        if (prompts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(prompts);
        }
        return ResponseEntity.ok(prompts);
    }

    @GetMapping("/{promptId}")
    public ResponseEntity<Object> getPromptById(@PathVariable Long promptId) {
        try{
            Prompt prompt = promptService.getPromptById(promptId);
            return ResponseEntity.ok(prompt);
        } catch (NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Prompt>> searchPrompts(@RequestBody SearchRequest searchRequest) {
        List<Prompt> searchedPrompts = promptService.searchPrompts(searchRequest);
        if (searchedPrompts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(searchedPrompts);
        }
        return ResponseEntity.ok(searchedPrompts);
    }

    @DeleteMapping("/delete/{promptId}")
    public ResponseEntity<String> deletePromptById(@PathVariable Long promptId) {
        try {
            User user = (User) securityService.getAuthenticatedUser();
            promptService.deletePrompt(promptId, user.getId());
            return ResponseEntity.noContent().build();  // 204 No Content - Successful deletion
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (AccessDeniedException e) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<String> deletePromptsByUserId(@PathVariable Long userId) {
        try {
            promptService.deletePromptsByUser(userId);
            return ResponseEntity.noContent().build(); // 204 No Content - Successful deletion
        } catch (NoResultException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}
