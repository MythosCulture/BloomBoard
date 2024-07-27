package com.bloomboard.promptboard.service;

import com.bloomboard.promptboard.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Tag findTag(String tag);
    void createTag(Tag tag);
    List<Tag> findOrphanedTags();
    void deleteOrphanedTags();
    Set<Tag> saveNewTags(String[] tags);
    String[] getFormattedTagsString (String tags);
}
