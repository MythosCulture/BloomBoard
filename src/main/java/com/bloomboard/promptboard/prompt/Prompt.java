package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.Tag;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "prompts")
@SequenceGenerator(name = "prompt_id_seq", allocationSize = 1)
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prompt_id_seq")
    private long id;
    private String title;
    private String summary;
    private String content;

    private boolean isActive;

    private OffsetDateTime createdAt;
    private OffsetDateTime lastModified;

    @ManyToMany
    @JoinTable(name = "tag_prompt",
            joinColumns = @JoinColumn(name = "prompt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Prompt(String title, String summary, String content, Set<Tag> tags ,User user, OffsetDateTime createdAt) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.tags = tags;
        this.user = user;
        this.createdAt = createdAt;
        this.lastModified = createdAt;
        this.isActive = true;
    }
    public Set<Tag> getTags() {
        return tags;
    }
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public void addTag(Tag tag){
        tags.add(tag);
    };
    public String[] getArrayTags() { //used on html templates
        Set<Tag> tags = this.getTags();
        String[] tagArray = new String[tags.size()];
        int i = 0;
        for (Tag tag : tags) {
            tagArray[i++] = tag.getTag();
        }
        return tagArray;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean bool) { isActive = bool; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getLastModified() { return lastModified; }
    public void setLastModified(OffsetDateTime lastModified) { this.lastModified = lastModified; }

    @Override
    public String toString() {
        return "Prompt{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", tags='" + getTags() + '\'' +
                '}';
    }
}
