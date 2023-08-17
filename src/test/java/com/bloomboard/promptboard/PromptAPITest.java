package com.bloomboard.promptboard;

import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.IPromptRepository;
import com.bloomboard.promptboard.prompt.PromptService;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import com.bloomboard.promptboard.tag.Tag;
import com.bloomboard.promptboard.tag.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PromptAPITest {
    @Autowired
    private IPromptRepository promptRepository;
    @Autowired
    private PromptService promptService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TagService tagService;

    private Prompt getPrompt() {
        //name, content, tags optional
        String title = "Monster Mash";
        String summary = "Greetings from the other side...";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String tag1 = "  testTagSpace             ";
        String tag2 = "Monster";
        String tag3 = "found family ";

        String[] promptTags = tagService.getFormattedTagsString(tag1 + "," + tag2 + "," + tag3);
        Set<Tag> tags = tagService.saveNewTags(promptTags);
        User user = userService.findByUsernameIgnoreCase("chonk");

        return new Prompt(title, summary, content, tags, user.getId(), OffsetDateTime.now());
    }
    @Test
    public void deletePromptTest() {
        // create a new prompt and save it to the repository
        Prompt prompt = getPrompt();
        promptRepository.save(prompt);

        // delete the prompt
        promptService.deletePrompt(prompt.getId());

        // ensure that the prompt no longer exists in the repository
        Optional<Prompt> deletedPrompt = promptRepository.findById(prompt.getId());
        assertFalse(deletedPrompt.isPresent());
    }

    @Test
    public void searchByPhraseTest() {
        //Define search phrase and retrieve matching prompts
        String phrase = "lorem ipsum";
        List<Prompt> searchedPrompts = promptRepository.findByPhrase(phrase.toLowerCase());
        //List<Prompt> searchedPrompts = promptRepository.findByPhrase_FullText(phrase.toLowerCase());

        // Print the searched prompts if any are found
        if (searchedPrompts.isEmpty()) {
            System.out.println("No prompts found for the given phrase.");
        } else {
            for (Prompt prompt : searchedPrompts) {
                System.out.println(prompt);
            }
        }

        for (Prompt prompt : searchedPrompts) {
            String summary = prompt.getSummary().toLowerCase();
            String content = prompt.getContent().toLowerCase();

            // Assert that the phrase is present in the summary or content
            assertTrue(summary.contains(phrase) || content.contains(phrase),
                    "Phrase not found in prompt: " + prompt.getId());
        }
    }

    @Test
    public void searchByTagTest() {
        List<String> tags = new ArrayList<>();
        tags.add("MONsters");

        //make sure every tag is lowercase
        for(int i = 0; i < tags.size(); i++ ) {
            tags.set(i, tags.get(i).toLowerCase());
        }

        List<Prompt> searchedPrompts = promptRepository.findByTagsIn(tags);

        // Print the searched prompts if any are found
        if (searchedPrompts.isEmpty()) {
            System.out.println("No prompts found for the given tags.");
        } else {
            for (Prompt prompt : searchedPrompts) {
                System.out.println("Prompt ID: " + prompt.getId());
                System.out.println("Tags: ");
                for (Tag tag : prompt.getTags()) {
                    System.out.println("- " + tag.getTag());
                }
                System.out.println("--------------------");
            }
        }

        for (Prompt prompt : searchedPrompts) {
            // Assert that the prompt has one of the tags in the list
            assertTrue(prompt.getTags().stream().anyMatch(tag -> tags.contains(tag.getTag().toLowerCase())),
                    "Prompt does not have any of the specified tags: " + prompt.getId());
        }
    }

}
