package com.bloomboard.promptboard.tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITagRepository extends JpaRepository<Tag, Long> {
    Tag findByTag(String tag);
    Tag findByTagIgnoreCase(String tag);
}
