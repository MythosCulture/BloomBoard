package com.bloomboard.promptboard;

import com.bloomboard.promptboard.prompt.Prompt;
import com.bloomboard.promptboard.prompt.IPromptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PromptAPITest {

    private Prompt getPrompt() {
        //name, content, tags optional
        String name = "Monster Mash";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String tag1 = "  testTagSpace             ";
        String tag2 = "Monster";
        String tag3 = "found family ";

        Prompt prompt = new Prompt(name, content, tag1, "chonk");
        prompt.addTags(tag2);
        prompt.addTags(tag3);

        return prompt;
    }

    @Autowired
    private IPromptRepository promptRepository;

    @Test
    public void findByTagsContainingIgnoreCase_Test(){
        Prompt prompt = getPrompt();
        promptRepository.save(prompt);
        System.out.println(prompt.getTags());

        String tagToFind = "Monster";

        List<Prompt> found = promptRepository.findByTagsContainingIgnoreCase(tagToFind);
        for(Prompt p: found) {
            System.out.println(p.getTags());
        }

        assertFalse(found.isEmpty());
        assertEquals(prompt.getTitle(), found.get(0).getTitle());
    }

}
