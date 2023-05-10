package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.Tag;
import com.bloomboard.promptboard.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PromptService {

    @Autowired
    private IPromptRepository promptRepository;
    @Autowired
    private TagService tagService;

    public List<Prompt> findAllPrompts(){
        return promptRepository.findAll();
    }

    public Prompt getPromptById(long id){
        Optional<Prompt> optionalPrompt = promptRepository.findById(id);

        return optionalPrompt.orElseThrow(IllegalArgumentException::new);
    }

    public void createPrompt (PromptRequest prompt, User user) {
        String[] promptTags = getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.updateTagRepo(promptTags);

        Prompt newPrompt = new Prompt(
                prompt.getTitle(),
                prompt.getContent(),
                tagSet,
                user
        );

        promptRepository.save(newPrompt);
    }

    public String[] getFormattedTagsString (String tags) {
        //remove whitespaces around commas
        Pattern whitespaceComma = Pattern.compile("\\s+\\,+\\s+|\\s+\\,");
        Matcher matcher = whitespaceComma.matcher(tags);
        String cleanTags = matcher.replaceAll(",");

        //remove whitespaces at ends of string
        Pattern whitespaceEnds = Pattern.compile("^\\s+|\\s+$");
        Matcher matcher2 = whitespaceEnds.matcher(cleanTags);
        cleanTags = matcher2.replaceAll("");

        //replace potential double comma and add comma to end if none exists
        Pattern endCommas = Pattern.compile("\\,+$"); //TODO: check if correct
        Matcher matcher3 = endCommas.matcher(cleanTags);
        cleanTags = matcher3.find() ? matcher3.replaceAll(",") : cleanTags + ",";

        return cleanTags.split(",");
    }

    public void updatePrompt (PromptRequest prompt, User user) {
        //TODO: This will need to remove created entries from joined table (?)
        Prompt updatedPrompt = getPromptById(prompt.getId());
        if (!updatedPrompt.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to edit this prompt.");
        }

        updatedPrompt.setTitle(prompt.getTitle());
        updatedPrompt.setContent(prompt.getContent());

        String[] promptTags = getFormattedTagsString(prompt.getTags());
        Set<Tag> tagSet = tagService.updateTagRepo(promptTags);
        updatedPrompt.setAppliedTags(tagSet);

        promptRepository.save(updatedPrompt);
    }

    public List<Prompt> findByTag(String tag) {
        return promptRepository.findByTagsContainingIgnoreCase(tag);
    }
    public List<Prompt> findByOwner(String owner) {
        return promptRepository.findByOwner(owner);
    }

    public void deletePrompt(long id) {
        Prompt prompt = promptRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prompt not found with id: " + id));
        promptRepository.delete(prompt);
    }
    //TODO: Add searchPhrase method
    //public List<Prompt> searchPhrase(String phrase) {}
}
