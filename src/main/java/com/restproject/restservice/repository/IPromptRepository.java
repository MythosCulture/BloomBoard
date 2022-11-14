package com.restproject.restservice.repository;

import com.restproject.restservice.domain.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findByName(String name);

    List<Prompt> findByTagsContaining(String tag);
    List<Prompt> findByTagsContainingIgnoreCase(String tag);

    //TODO: Add "search by phrase" for Name, Contents, and Tags

}
