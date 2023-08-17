package com.bloomboard.promptboard.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TagService {
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    @Autowired
    private ITagRepository tagRepository;

    public Tag findTag(String tag) {
       return tagRepository.findByTagIgnoreCase(tag);
    }

    public void createTag(Tag tag) {
        tagRepository.save(tag);
    }

    public List<Tag> findOrphanedTags() { return tagRepository.findOrphanedTags(); }
    public void deleteOrphanedTags() {
        List<Tag> orphanedTags = findOrphanedTags();
        if (!orphanedTags.isEmpty()) {
            logger.info("Deleted Orphaned Tags: " + orphanedTags);
            for (Tag tag: orphanedTags) tagRepository.delete(tag);
        }
    }

    /* Create new tags in repository if they don't exist. Add everything to tagSet */
    public Set<Tag> saveNewTags(String[] tags) {
        Set<Tag> tagSet = new HashSet<>();
        for(String tag: tags) {
            Tag existingTag = findTag(tag);
            if(existingTag == null) {
                Tag newTag = new Tag(tag);
                createTag(newTag);
                tagSet.add(newTag);
            } else {
                tagSet.add(existingTag);
            }
        }
        return tagSet;
    }

    public String[] getFormattedTagsString (String tags) {
        //remove whitespaces around commas
        Pattern whitespaceComma = Pattern.compile("\\s*,\\s*(?=\\S|$)");
        Matcher matcher = whitespaceComma.matcher(tags);
        String cleanTags = matcher.replaceAll(",");

        //remove whitespaces at ends of string
        Pattern whitespaceEnds = Pattern.compile("^\\s+|\\s+$");
        Matcher matcher2 = whitespaceEnds.matcher(cleanTags);
        cleanTags = matcher2.replaceAll("");

        //replace potential double comma and add comma to end if none exists
        Pattern endCommas = Pattern.compile(",{2,}|,+$");
        Matcher matcher3 = endCommas.matcher(cleanTags);
        cleanTags = matcher3.find() ? matcher3.replaceAll(",") : cleanTags + ",";

        return cleanTags.split(",");
    }
}
