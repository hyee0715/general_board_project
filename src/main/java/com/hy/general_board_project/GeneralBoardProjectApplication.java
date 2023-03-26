package com.hy.general_board_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class GeneralBoardProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneralBoardProjectApplication.class, args);
	}

}
