package com.igym;

import com.igym.entity.Employee;
import com.igym.repository.EmployeeRepository;
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
    public CommandLineRunner initializeAdmin(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        return args -> {
            // Check if admin exists
            if (employeeRepository.count() == 0) {
                // Create admin user
                employeeService.createEmployee(
                    "System Administrator",
                    "Admin",
                    "admin123",
                        "Default",// You should change this password in production,
                        Employee.Role.MANAGER
                );
                System.out.println("Admin user created successfully!");
                System.out.println("Username: Admin");
                System.out.println("Password: admin123");
            }
        };
    }
}
