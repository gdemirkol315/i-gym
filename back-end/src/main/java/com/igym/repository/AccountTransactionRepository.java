package com.igym.repository;

import com.igym.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
    List<AccountTransaction> findByCustomerId(Long customerId);
    
    List<AccountTransaction> findByCustomerIdAndDateBetween(
        Long customerId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    @Query("SELECT SUM(CASE WHEN t.transactionType = 'CREDIT' THEN t.amount ELSE -t.amount END) " +
           "FROM AccountTransaction t WHERE t.customer.id = ?1")
    Double calculateBalance(Long customerId);
    
    List<AccountTransaction> findByCustomerIdAndTransactionType(
        Long customerId,
        AccountTransaction.TransactionType type
    );
    
    List<AccountTransaction> findByReferenceTypeAndReferenceId(
        AccountTransaction.ReferenceType referenceType,
        Long referenceId
    );
}
