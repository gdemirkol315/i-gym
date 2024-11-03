package com.igym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IGymApplication {
    public static void main(String[] args) {
        SpringApplication.run(IGymApplication.class, args);
    }
}
