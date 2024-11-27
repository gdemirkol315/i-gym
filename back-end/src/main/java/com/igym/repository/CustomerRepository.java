package com.igym.repository;

import com.igym.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByBarcode(String qrCode);
    boolean existsByEmail(String email);
    boolean existsByBarcode(String barcode);
    List<Customer> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);
}
