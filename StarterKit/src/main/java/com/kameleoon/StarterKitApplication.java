package com.kameleoon;

import com.kameleoon.config.KameleoonProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(KameleoonProperties.class)
@SpringBootApplication
public class StarterKitApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarterKitApplication.class, args);
    }
}