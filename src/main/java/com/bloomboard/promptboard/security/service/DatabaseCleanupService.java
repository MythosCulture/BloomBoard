package com.bloomboard.promptboard.security.service;

import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.PromptService;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupService.class);

    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final PromptService promptService;
    @Autowired
    private final TagService tagService;

    //@Scheduled(fixedRate = 5000) // Schedule to run every 5 seconds
    public void testScheduledMethod() {
        logger.info("TEST MESSAGE SENT!!!!");
    }

    @Scheduled(cron = "0 0 0 * * ?") //Everynight at midnight
    public void performDailyCleanup() {
        try {
            logger.info("Performing daily cleanup...");

            //Delete posts from demo account
            User demoUser = userService.findByUsernameIgnoreCase("DemoUser");
            List<Prompt> demoPrompts = promptService.findByUser_id(demoUser.getId());
            if(!demoPrompts.isEmpty()) {
                for (Prompt prompt: demoPrompts) {
                    promptService.deletePrompt(prompt.getId());
                }
                logger.info("\tDeleted " + demoPrompts.size() + " prompts from the DemoUser account.");
                logger.info("\tDeleted Prompts: \n\t\t" + demoPrompts);
            } else {
                logger.info("\tNo prompts deleted from DemoUser account");
            }

            //delete tags not associated with a prompt
            tagService.deleteOrphanedTags();

            logger.info("Daily cleanup completed successfully.");
        } catch (Exception e){
            logger.error("An error occurred during daily cleanup:", e);
        }
    }
}
