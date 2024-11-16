package com.igym.security;

import com.igym.entity.Employee;
import com.igym.entity.Salt;
import com.igym.repository.EmployeeRepository;
import com.igym.repository.SaltRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final SaltRepository saltRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

        Salt salt = saltRepository.findByEmployeeId(employee.getId())
                .orElseThrow(() -> new RuntimeException("Salt not found for employee"));

        // The stored password in UserDetails must be the hashed version that matches what we'll create
        // from the salted password during authentication
        return new User(
                employee.getEmail(),
                employee.getCredentials().getHashedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
        );
    }
}
