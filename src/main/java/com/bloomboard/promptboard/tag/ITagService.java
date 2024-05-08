package com.bloomboard.promptboard.tag;

import java.util.List;
import java.util.Set;

public interface ITagService {
    Tag findTag(String tag);
    void createTag(Tag tag);
    List<Tag> findOrphanedTags();
    void deleteOrphanedTags();
    Set<Tag> saveNewTags(String[] tags);
    String[] getFormattedTagsString (String tags);
}
