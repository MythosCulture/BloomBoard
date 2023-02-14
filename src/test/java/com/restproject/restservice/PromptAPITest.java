package com.restproject.restservice;

import com.restproject.restservice.domain.Prompt;
import com.restproject.restservice.repository.IPromptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PromptAPITest {

    private Prompt getPrompt() {
        //name, content, tags optional
        String name = "GrimDark Fantasy";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String tag1 = "  ipsum             ";
        String tag2 = "Elves,";
        String tag3 = "Family";
        Prompt prompt = new Prompt(name, content, tag1);

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

        String tagToFind = "elves";

        List<Prompt> found = promptRepository.findByTagsContainingIgnoreCase(tagToFind +",");
        for(Prompt p: found) {
            System.out.println(p.getTags());
        }

        assertFalse(found.isEmpty());
        assertEquals(prompt.getName(), found.get(0).getName());
    }

}
