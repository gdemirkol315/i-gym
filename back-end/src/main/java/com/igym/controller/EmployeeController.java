package com.igym.controller;

import com.igym.entity.Employee;
import com.igym.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    public ResponseEntity<Employee> getProfile(Authentication authentication) {
        Employee employee = employeeService.getEmployeeProfile(authentication.getName());
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {
        boolean success = employeeService.changePassword(
                authentication.getName(),
                request.getOldPassword(),
                request.getNewPassword()
        );

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Invalid old password");
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        Employee employee = employeeService.createEmployee(
            request.getName(),
            request.getLastName(),
            request.getAddress(),
            request.getEmail(),
            request.getPosition(),
            Employee.Role.EMPLOYEE
        );
        return ResponseEntity.ok(employee);
    }

    @Data
    public static class CreateEmployeeRequest {
        @NotBlank(message = "Name is required")
        private String name;

        private String lastName;

        private String address;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Position is required")
        private String position;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotBlank(message = "Old password is required")
        private String oldPassword;

        @NotBlank(message = "New password is required")
        private String newPassword;
    }
}
