package com.igym.repository;

import com.igym.entity.EntryProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryProductTransactionRepository extends JpaRepository<EntryProductTransaction, Long> {
    List<EntryProductTransaction> findByCustomerIdAndIsActiveTrue(Long customerId);
    
    List<EntryProductTransaction> findByCustomerIdAndExpiryDateAfterAndIsActiveTrue(
        Long customerId, 
        LocalDateTime currentDate
    );
    
    Optional<EntryProductTransaction> findFirstByCustomerIdAndEntryProduct_EntryTypeAndIsActiveTrueOrderByExpiryDateDesc(
        Long customerId,
        String entryType
    );
    
    List<EntryProductTransaction> findByExpiryDateBeforeAndIsActiveTrue(LocalDateTime date);
}
