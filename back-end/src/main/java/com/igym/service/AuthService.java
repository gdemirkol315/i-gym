package com.igym.service;

import com.igym.dto.auth.LoginRequest;
import com.igym.dto.auth.LoginResponse;
import com.igym.entity.Employee;
import com.igym.entity.Salt;
import com.igym.repository.EmployeeRepository;
import com.igym.repository.SaltRepository;
import com.igym.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final SaltRepository saltRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        // First find the employee by username
        Employee employee = employeeRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Employee not found with username: " + request.getUsername()));

        // Get the salt for this employee
        Salt salt = saltRepository.findByEmployeeId(employee.getId())
            .orElseThrow(() -> new RuntimeException("Salt not found for employee"));

        // Create UserDetails for token generation
        UserDetails userDetails = new User(
            employee.getCredentials().getUsername(),
            employee.getCredentials().getHashedPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
        );

        // Combine password with salt (but don't encode it - let DaoAuthenticationProvider do that)
        String saltedPassword = request.getPassword() + salt.getSalt();

        // Authenticate with raw salted password
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                saltedPassword
            )
        );

        // Generate token
        String token = jwtUtil.generateToken(userDetails);

        return LoginResponse.builder()
            .token(token)
            .employeeId(employee.getId())
            .name(employee.getName())
            .role(employee.getRole())
            .position(employee.getPosition())
            .build();
    }
}
