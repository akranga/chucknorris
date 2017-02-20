package com.github.akranga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class ChuckNorrisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChuckNorrisApplication.class, args);
    }
}
