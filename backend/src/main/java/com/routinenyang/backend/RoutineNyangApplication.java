package com.routinenyang.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RoutineNyangApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutineNyangApplication.class, args);
	}

}
