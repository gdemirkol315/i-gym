package com.igym.service;

import com.igym.dto.auth.LoginRequest;
import com.igym.dto.auth.LoginResponse;
import com.igym.entity.Employee;
import com.igym.repository.EmployeeRepository;
import com.igym.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Employee employee = employeeRepository.findByCredentials_HashedPassword(userDetails.getPassword())
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        return LoginResponse.builder()
            .token(token)
            .employeeId(employee.getId())
            .name(employee.getName())
            .role(employee.getRole())
            .position(employee.getPosition())
            .build();
    }
}
