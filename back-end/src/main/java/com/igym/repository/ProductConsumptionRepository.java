package com.igym.repository;

import com.igym.entity.ProductConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductConsumptionRepository extends JpaRepository<ProductConsumption, Long> {
    List<ProductConsumption> findByCustomerId(Long customerId);
    
    List<ProductConsumption> findByCustomerIdAndDateBetween(
        Long customerId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    List<ProductConsumption> findByProductIdAndDateBetween(
        Long productId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}
