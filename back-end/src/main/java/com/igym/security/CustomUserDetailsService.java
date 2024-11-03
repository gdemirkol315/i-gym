package com.igym.security;

import com.igym.entity.Employee;
import com.igym.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String hashedPassword) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByCredentials_HashedPassword(hashedPassword)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        return new User(
                employee.getId().toString(),
                employee.getCredentials().getHashedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()))
        );
    }
}
