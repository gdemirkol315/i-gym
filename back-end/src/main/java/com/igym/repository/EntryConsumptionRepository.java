package com.igym.repository;

import com.igym.entity.EntryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryConsumptionRepository extends JpaRepository<EntryConsumption, Long> {
    List<EntryConsumption> findByCustomerIdAndIsActiveTrue(Long customerId);
    
    List<EntryConsumption> findByCustomerIdAndExpiryDateAfterAndIsActiveTrue(
        Long customerId, 
        LocalDateTime currentDate
    );
    
    Optional<EntryConsumption> findFirstByCustomerIdAndEntryProduct_EntryTypeAndIsActiveTrueOrderByExpiryDateDesc(
        Long customerId,
        String entryType
    );
    
    List<EntryConsumption> findByExpiryDateBeforeAndIsActiveTrue(LocalDateTime date);
}
