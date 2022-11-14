package com.restproject.restservice.domain;

import javax.persistence.*;

@Entity
@Table(name = "prompts")
public class Prompt {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String content;
    private String tags;

    public Prompt() {
    }

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
    private String formatStringToTags(String tags) {
        String[] commaSplit = tags.split(",");
        String formattedString = "";

        for(String str: commaSplit) {
            String newString = str.replaceAll("^\\s+|\\s+$", "");
            formattedString += " " + newString +",";
        }

        return formattedString;
    }

    public void setTags(String tags) {
        this.tags = formatStringToTags(tags);
    }

    public void addTags(String tags) {
        this.tags += formatStringToTags(tags);
    }

    public void removeTag(String tag) {
        if(!tag.contains(",") && this.tags.contains(tag)) {
            //TODO: add remove tag logic
        }
    }
}
