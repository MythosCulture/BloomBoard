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
        String name = "Monster Mash";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String tag1 = "  testTagSpace             ";
        String tag2 = "Monster";
        String tag3 = "found family ";

        String[] promptTags = promptService.getFormattedTagsString(tag1 + "," + tag2 + "," + tag3);
        Set<Tag> tags = tagService.updateTagRepo(promptTags);
        Optional<User> user = userService.findByUsernameIgnoreCase("chonk");

        return new Prompt(name, content, tags, user.get());
    }

    @Test
    public void findByTagsContainingIgnoreCase_Test(){
        Prompt prompt = getPrompt();
        promptRepository.save(prompt);
        System.out.println(prompt.getAppliedTags());

        //Save each tag separately in tag table before searching prompt by tag
        Set<Tag> tags = prompt.getAppliedTags();
        for (Tag tag : tags) {
            tagService.createTag(tag);
        }

        String tagToFind = "Monster";

        List<Prompt> found = promptRepository.findByTagsContainingIgnoreCase(tagToFind);
        for(Prompt p: found) {
            System.out.println(p.getAppliedTags());
        }

        assertFalse(found.isEmpty());
        assertEquals(prompt.getTitle(), found.get(0).getTitle());
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

}
