package com.igym.dto;

import com.igym.entity.Employee;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String lastName;
    private String address;
    private String position;
    private Employee.Role role;
    private String email;
    private LocalDateTime createdAt;

    public static EmployeeDTO fromEntity(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setLastName(employee.getLastName());
        dto.setAddress(employee.getAddress());
        dto.setPosition(employee.getPosition());
        dto.setRole(employee.getRole());
        dto.setEmail(employee.getEmail());
        dto.setCreatedAt(employee.getCreatedAt());
        return dto;
    }
}
