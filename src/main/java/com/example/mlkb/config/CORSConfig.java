package com.example.mlkb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH","OPTIONS")
//			.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
			.allowedHeaders("*")
			.allowedOrigins("http://localhost:4200")
			.allowCredentials(true);
		//TODO: Need to change the URL for the production URL when we deploy
	}


}
