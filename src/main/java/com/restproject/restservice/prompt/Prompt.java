package com.restproject.restservice.prompt;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Entity
@Table(name = "prompts")
public class Prompt {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String content;
    private String tags;

    public Prompt(String name, String content, String tags) {
        this.name = name;
        this.content = content;
        setTags(tags);
    }

    public long getId() {
        return id;
    }

    //Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        //TODO: Set upper character limit
        this.name = name;
    }

    //Content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //Tags
    public String getTags() {
        return tags;
    }

    private String formatTags(String tags) {
        Pattern whitespaceComma = Pattern.compile("\\s+\\,+\\s+|\\s+\\,");
        Pattern whitespaceEnds = Pattern.compile("^\\s+|\\s+$");
        Pattern endCommas = Pattern.compile("\\,+$");

        Matcher matcher = whitespaceComma.matcher(tags);
        String cleanTags = matcher.replaceAll(",");

        Matcher matcher2 = whitespaceEnds.matcher(cleanTags);
        cleanTags = matcher2.replaceAll("");

        Matcher matcher3 = endCommas.matcher(cleanTags);
        if (matcher3.find()) { //replace potential double comma and add comma to end if none exists
            matcher3.replaceAll(",");
        } else {
            cleanTags += ",";
        }

        return cleanTags;
    }

    //Sets all tags; will write over pre-existing tags
    public void setTags(String tags) { this.tags = formatTags(tags); }

    //Adds one or more tag to end of tag string, separated by space and comma
    public void addTags(String tags) { this.tags += formatTags(tags); }

    public void removeTag(String tag) {
        String tagToRemove = formatTags(tag);
        if (this.tags.contains(tagToRemove)) {
            String currentTags = this.getTags();
            String updatedTags = currentTags.replace(tagToRemove, "");
            this.setTags(updatedTags);
        }
    }

    @Override
    public String toString() {
        return "Prompt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
