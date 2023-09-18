package com.example.back_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class BackProjectApplication {



	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:8081")
				.allowedMethods(
						HttpMethod.GET.name(),
						HttpMethod.HEAD.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name());
	}

	public static void main(String[] args) {
		SpringApplication.run(BackProjectApplication.class, args);
	}
}
