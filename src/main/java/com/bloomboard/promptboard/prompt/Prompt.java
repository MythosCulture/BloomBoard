package com.bloomboard.promptboard.prompt;

import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.tag.Tag;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String content;

    @ManyToMany
    @JoinTable(name = "tag_prompt",
            joinColumns = @JoinColumn(name = "prompt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> appliedTags = new HashSet<>();
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Prompt(String title, String content, Set<Tag> tags ,User user) {
        this.title = title;
        this.content = content;
        this.appliedTags = tags;
        this.user = user;
    }
    public Set<Tag> getAppliedTags() {
        return appliedTags;
    }
    public void setAppliedTags(Set<Tag> appliedTags) {
        this.appliedTags = appliedTags;
    }
    public void addTag(Tag tag){
        appliedTags.add(tag);
    };

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    //Name
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        //TODO: Set upper character limit
        this.title = title;
    }

    //Content
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    //TODO: Unnecessary? Would need to replace deleted methods.
    /*
    public void removeTag(String tag) {
        String tagToRemove = formatTags(tag);
        if (this.tags.contains(tagToRemove)) {
            String currentTags = this.getTags();
            String updatedTags = currentTags.replace(tagToRemove, "");
            this.setTags(updatedTags);
        }
    }
     */
    @Override
    public String toString() {
        return "Prompt{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags='" + getAppliedTags() + '\'' +
                '}';
    }
}
