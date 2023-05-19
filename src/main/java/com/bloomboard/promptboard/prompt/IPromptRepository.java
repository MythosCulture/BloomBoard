package com.bloomboard.promptboard.prompt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findByUser_id(long user_id);

    //TODO: implement tag search on prompts
    //TODO: Add "search by phrase" for Name, Contents, and Tags

}
