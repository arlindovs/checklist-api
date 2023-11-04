package com.learning.springboot.checklistapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChecklistApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ChecklistApiApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Checklist API")
						.version("1.0.0")
						.description("Checklist API v1.0.0")
				);
	}

}
