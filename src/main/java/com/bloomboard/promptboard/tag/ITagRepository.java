package com.bloomboard.promptboard.tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITagRepository extends JpaRepository<Tag, Long> {
    Tag findByTag(String tag);
    Tag findByTagIgnoreCase(String tag);
    @Query("SELECT t FROM Tag t WHERE t NOT IN (SELECT DISTINCT tag FROM Prompt p JOIN p.tags tag)")
    List<Tag> findOrphanedTags();
}
