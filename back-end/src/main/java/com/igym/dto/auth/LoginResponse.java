package com.igym.dto.auth;

import com.igym.entity.Employee;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private Long employeeId;
    private String name;
    private Employee.Role role;
    private String position;
}
