package com.igym.repository;

import com.igym.entity.Salt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaltRepository extends JpaRepository<Salt, Long> {
    Optional<Salt> findByEmployeeId(Long employeeId);
    boolean existsByEmployeeId(Long employeeId);
}
