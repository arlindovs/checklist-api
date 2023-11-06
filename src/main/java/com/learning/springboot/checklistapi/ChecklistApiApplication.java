package com.learning.springboot.checklistapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ChecklistApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ChecklistApiApplication.class, args);
	}

	@Profile("local")
	@Bean
	public WebMvcConfigurer corsLocalConfig() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
						.allowedHeaders("Origin","X-Requested-With", "Content-Type", "Accept", "Authorization")
						.allowedOrigins("http://localhost:4200");
			}
		};
	}

	@Profile("aws")
	@Bean
	public WebMvcConfigurer corsAwsConfig() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("Origin","X-Requested-With", "Content-Type", "Accept", "Authorization")
						.allowedOrigins("http://checklist-spa.s3-website-sa-east-1.amazonaws.com");
			}
		};
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
