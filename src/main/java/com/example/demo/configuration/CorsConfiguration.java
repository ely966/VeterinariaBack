package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("**")
				.allowedOrigins("http://localhost:4200")
				.allowedHeaders("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD","HEADER")
				.allowedMethods("GET", "POST", "PUT", "DELETE");
				//maxAge(3600)
			}
		};
	}

}
