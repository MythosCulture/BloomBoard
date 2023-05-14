package com.bloomboard.promptboard.prompt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findByUser_id(long user_id);
    //List<Prompt> findByTagsContaining(String tag);

    //TODO: implement tag search on prompts
    //List<Prompt> findByTagsContainingIgnoreCase(String tag);

    //TODO: Add "search by phrase" for Name, Contents, and Tags

}
