package com.mlkb.ftm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String version = "0.1";

    @Bean
    public OpenAPI ftmOpenApi() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Follow the money API")
                .description("API for manage budget app")
                .version(this.version)
                .contact(apiContact());
    }

    private Contact apiContact() {
        return new Contact()
                .name("Michał Lechowicz")
                .url("https://github.com/ichal6");
    }

}
