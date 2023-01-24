package com.mlkb.ftm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class FollowTheMoneyApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FollowTheMoneyApplication.class);
    }

    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }

    public static void main(String[] args) {
        SpringApplication.run(FollowTheMoneyApplication.class, args);
    }

}
