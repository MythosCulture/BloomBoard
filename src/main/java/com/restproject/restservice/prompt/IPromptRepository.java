package com.restproject.restservice.prompt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findByOwner(String owner);
    List<Prompt> findByTagsContaining(String tag);
    List<Prompt> findByTagsContainingIgnoreCase(String tag);

    //TODO: Add "search by phrase" for Name, Contents, and Tags

}
