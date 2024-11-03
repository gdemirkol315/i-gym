package com.igym.repository;

import com.igym.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCredentials_HashedPassword(String hashedPassword);
    boolean existsByName(String name);
}
