package com.restproject.restservice.prompt;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PromptRequest {

    @NotEmpty
    @Size(min=4, max=60)
    String title;
    @NotEmpty
    String tags;
    @NotEmpty
    @Size(min=50, max=7000)
    String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PromptRequestModel [title=" + title + ", tags=" + tags + ", content=" + content + "]";
    }
}
