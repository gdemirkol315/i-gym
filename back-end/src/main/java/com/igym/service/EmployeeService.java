package com.igym.service;

import com.igym.entity.Employee;
import com.igym.entity.Credentials;
import com.igym.entity.Salt;
import com.igym.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee createEmployee(String name, String lastName, String address, String email, String position, Employee.Role role) {
        if (employeeRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Generate a random password
        String password = generateRandomPassword();

        //todo delete
        if (email.equals("admin@admin.com")){
            password = "123456";
        }

        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        String saltStr = Base64.getEncoder().encodeToString(saltBytes);

        // Create employee
        Employee employee = new Employee();
        employee.setName(name);
        employee.setLastName(lastName);
        employee.setAddress(address);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setRole(role);

        // Create and set salt
        Salt salt = new Salt();
        salt.setEmployee(employee);
        salt.setSalt(saltStr);
        employee.setSalt(salt);

        // Create and set credentials with salted and hashed password
        String saltedPassword = password + saltStr;
        String hashedPassword = passwordEncoder.encode(saltedPassword);
        
        Credentials credentials = new Credentials();
        credentials.setEmployee(employee);
        credentials.setHashedPassword(hashedPassword);
        employee.setCredentials(credentials);

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Send email with credentials
        emailService.sendPasswordEmail(email, password);

        return savedEmployee;
    }

    public Employee getEmployeeProfile(String email) {
        return employeeRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
    }

    @Transactional
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        String saltStr = employee.getSalt().getSalt();
        String oldSaltedPassword = oldPassword + saltStr;
        
        // Verify old password
        if (!passwordEncoder.matches(oldSaltedPassword, employee.getCredentials().getHashedPassword())) {
            return false;
        }

        // Update to new password
        String newSaltedPassword = newPassword + saltStr;
        String newHashedPassword = passwordEncoder.encode(newSaltedPassword);
        employee.getCredentials().setHashedPassword(newHashedPassword);
        employeeRepository.save(employee);
        
        return true;
    }

    private String generateRandomPassword() {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        // Generate a 12-character password
        for (int i = 0; i < 12; i++) {
            password.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        
        return password.toString();
    }

    @Transactional
    public void createInitialAdminAccount() {
        if (!employeeRepository.existsByEmail("admin@admin.com")) {
            createEmployee(
                "Initial",
                "Admin",
                "Admin Address",
                "admin@admin.com",
                "Administrator",
                Employee.Role.MANAGER
            );
        }
    }
}
