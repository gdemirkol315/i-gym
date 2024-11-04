package com.igym.service;

import com.igym.entity.Employee;
import com.igym.entity.Credentials;
import com.igym.entity.Salt;
import com.igym.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Employee createEmployee(String name, String position, Employee.Role role, String password) {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        String saltStr = Base64.getEncoder().encodeToString(saltBytes);

        // Create employee
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        employee.setRole(role);

        // Create and set salt
        Salt salt = new Salt();
        salt.setEmployee(employee);
        salt.setSalt(saltStr);
        employee.setSalt(salt);

        // Create and set credentials with hashed password
        String hashedPassword = passwordEncoder.encode(password + saltStr);
        Credentials credentials = new Credentials();
        credentials.setEmployee(employee);
        credentials.setHashedPassword(hashedPassword);
        employee.setCredentials(credentials);

        return employeeRepository.save(employee);
    }
}
