package com.example.back_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class BackProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackProjectApplication.class, args);
	}
}
