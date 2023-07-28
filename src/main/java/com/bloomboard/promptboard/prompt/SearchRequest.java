package com.bloomboard.promptboard.prompt;

import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchRequest {
    @Size(max = 3, message = "Can not search more than 3 tags at once.")
    private List<@Size(max = 60) String> tags;
    @Size(max = 255)
    private String phrase;

    public List<String> getTags() {
        return tags;
    }
    public void setTags(String tags) {
        if (tags == null || tags.isEmpty()) {
            this.tags = Collections.emptyList();
        } else {
            List<String> tagList = Arrays.stream(tags.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            this.tags = tagList;
        }
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPhrase() {
        return phrase;
    }
    public void setPhrase(String phrase) {
        this.phrase = phrase != null ? phrase.trim() : null;
    }
}
