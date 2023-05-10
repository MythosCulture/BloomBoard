package com.bloomboard.promptboard.tag;

import com.bloomboard.promptboard.prompt.Prompt;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "tags")
@SequenceGenerator(name = "tag_id_seq", allocationSize = 1)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    private long id;
    private String tag;
    @JsonIgnore
    @ManyToMany(mappedBy = "appliedTags") //name needs to correspond w/ name of set in Prompt model
    private Set<Prompt> appliedPrompts = new HashSet<>();

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Set<Prompt> getAppliedPrompts() {
        return appliedPrompts;
    }


}
