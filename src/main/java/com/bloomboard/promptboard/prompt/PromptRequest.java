package com.bloomboard.promptboard.prompt;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PromptRequest {
    private Long id;
    @NotEmpty
    @Size(min=4, max=60)
    private String title;

    @NotEmpty
    private String tags;
    @NotEmpty
    @Size(min=50, max=7000)
    private String content;

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

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "PromptRequestModel [title=" + title + ", tags=" + tags + ", content=" + content + "]";
    }
}
