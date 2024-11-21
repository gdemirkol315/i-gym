package com.igym.service;

import com.igym.entity.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee createEmployee(String name, String lastName, String address, String email, Employee.Role role);
    Employee getEmployeeProfile(String email);
    boolean changePassword(String email, String oldPassword, String newPassword);
    Employee updateProfile(String email, String phone, String address);
    Employee updateRole(Long employeeId, Employee.Role newRole);
    void createInitialAdminAccount();
}
