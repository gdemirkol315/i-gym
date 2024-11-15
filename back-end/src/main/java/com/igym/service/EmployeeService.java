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

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Employee createEmployee(String name, String username, String password, String position, Employee.Role role) {
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

        // Create and set credentials with salted and hashed password
        String saltedPassword = password + saltStr;
        String hashedPassword = passwordEncoder.encode(saltedPassword);
        
        Credentials credentials = new Credentials();
        credentials.setEmployee(employee);
        credentials.setUsername(username);
        credentials.setHashedPassword(hashedPassword);
        employee.setCredentials(credentials);

        return employeeRepository.save(employee);
    }

    public Employee getEmployeeProfile(String username) {
        return employeeRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findByUsername(username)
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
}
