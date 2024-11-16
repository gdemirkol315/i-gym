package com.igym;

import com.igym.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IGymApplication {

    public static void main(String[] args) {
        SpringApplication.run(IGymApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeAdmin(EmployeeService employeeService) {
        return args -> {
            employeeService.createInitialAdminAccount();
        };
    }
}
