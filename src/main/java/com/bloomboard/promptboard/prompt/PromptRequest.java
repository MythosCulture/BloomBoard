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
    @Size(max = 255)
    private String summary;
    @NotEmpty
    @Size(min=50, max=7000)
    private String content;
    //passed to createdAt and/or lastModified
    @NotEmpty
    private String submissionDate;

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

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String date) {this.submissionDate = date;}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "PromptRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", submissionDate='" + submissionDate + '\'' +
                '}';
    }
}
