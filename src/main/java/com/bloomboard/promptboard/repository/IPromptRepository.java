package com.bloomboard.promptboard.repository;

import com.bloomboard.promptboard.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPromptRepository extends JpaRepository<Prompt, Long> {

    @Query("SELECT p FROM Prompt p WHERE LOWER(p.summary) LIKE %:phrase% OR LOWER(p.content) LIKE %:phrase%")
    List<Prompt> findByPhrase(@Param("phrase") String phrase);

    //'nativeQuery = true' allows SQL search on string entered as value attribute
    /*
    //TODO: add Fulltext search
    @Query(value = "SELECT * FROM prompts WHERE summary = :phrase OR content = :phrase",
            nativeQuery = true)
    List<Prompt> findByPhrase_FullText(@Param("phrase")String phrase);
     */
    @Query("SELECT p FROM Prompt p LEFT JOIN p.tags t WHERE LOWER(t.tag) IN :tags")
    List<Prompt> findByTagsIn(@Param("tags") List<String> tags);
    List<Prompt> findByUserId(long userId);

}
