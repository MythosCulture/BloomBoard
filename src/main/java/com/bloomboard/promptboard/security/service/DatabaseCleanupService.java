package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.PromptService;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseCleanupService {

    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final PromptService promptService;

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    public void performDailyCleanup() {
        UserRole demoRole = UserRole.DEMO;
        List<User> demoUsers = userService.findByUserRole(demoRole);

        for ( User user: demoUsers) {
            List<Prompt> demoPrompts = promptService.findByUser_id(user.getId());
            for (Prompt prompt: demoPrompts) {
                promptService.deletePrompt(prompt.getId());
            }
        }

    }
}
