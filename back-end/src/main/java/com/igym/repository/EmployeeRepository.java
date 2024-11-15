package com.igym.repository;

import com.igym.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCredentials_HashedPassword(String hashedPassword);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.credentials c LEFT JOIN FETCH e.salt WHERE c.username = :username")
    Optional<Employee> findByUsername(@Param("username") String username);


    @Query("SELECT e FROM Employee e WHERE e.credentials.username = :username")
    Optional<Employee> getEmployee(@Param("username") String username);


    boolean existsByName(String name);
}
