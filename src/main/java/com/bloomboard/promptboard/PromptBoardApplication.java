package com.bloomboard.promptboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PromptBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromptBoardApplication.class, args);
	}

}
