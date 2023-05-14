package com.bloomboard.promptboard.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {
    @Autowired
    private ITagRepository tagRepository;

    public Tag getTag(String tag) {
       return tagRepository.findByTagIgnoreCase(tag);
    }

    public void createTag(Tag tag) {
        tagRepository.save(tag);
    }

    /* Create new tags in repository if they don't exist. Add everything to tagSet */
    public Set<Tag> saveNewTags(String[] tags) {
        Set<Tag> tagSet = new HashSet<>();
        for(String tag: tags) {
            if(getTag(tag).getTag() != tag) {
                Tag newTag = new Tag(tag);
                createTag(newTag);
                tagSet.add(newTag);
            } else {
                tagSet.add(getTag(tag));
            }
        }
        return tagSet;
    }
}
