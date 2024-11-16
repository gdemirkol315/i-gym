package com.igym.controller;

import com.igym.dto.EmployeeDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            List<EmployeeDTO> employeeDTOs = employees.stream()
                    .map(EmployeeDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(employeeDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> getProfile(Authentication authentication) {
        Employee employee = employeeService.getEmployeeProfile(authentication.getName());
        return ResponseEntity.ok(EmployeeDTO.fromEntity(employee));
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

    @PostMapping("/update-profile")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        Employee employee = employeeService.updateProfile(
                authentication.getName(),
                request.getPhone(),
                request.getAddress()
        );
        return ResponseEntity.ok(EmployeeDTO.fromEntity(employee));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        try {
            Employee employee = employeeService.createEmployee(
                    request.getName(),
                    request.getLastName(),
                    request.getAddress(),
                    request.getEmail(),
                    request.getRole()
            );
            return ResponseEntity.ok(EmployeeDTO.fromEntity(employee));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
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

        private Employee.Role role;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotBlank(message = "Old password is required")
        private String oldPassword;

        @NotBlank(message = "New password is required")
        private String newPassword;
    }

    @Data
    public static class UpdateProfileRequest {
        private String phone;
        private String address;
    }
}
