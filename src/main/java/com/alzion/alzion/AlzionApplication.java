package com.alzion.alzion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlzionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlzionApplication.class, args);
	}

}
