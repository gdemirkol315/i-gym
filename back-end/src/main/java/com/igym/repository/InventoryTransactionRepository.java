package com.igym.repository;

import com.igym.entity.InventoryTransaction;
import com.igym.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    List<InventoryTransaction> findByInventoryId(Long inventoryId);
    
    List<InventoryTransaction> findByInventoryIdAndType(Long inventoryId, TransactionType type);
    
    List<InventoryTransaction> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<InventoryTransaction> findBySaleId(Long saleId);
    
    List<InventoryTransaction> findByEmployeeId(Long employeeId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.inventory.id = :inventoryId " +
           "AND t.timestamp BETWEEN :startDate AND :endDate")
    List<InventoryTransaction> findTransactionsByDateRange(
        @Param("inventoryId") Long inventoryId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT SUM(CASE WHEN t.type = 'STOCK_IN' THEN t.quantity ELSE -t.quantity END) " +
           "FROM InventoryTransaction t WHERE t.inventory.id = :inventoryId " +
           "AND t.timestamp BETWEEN :startDate AND :endDate")
    Integer calculateStockMovement(
        @Param("inventoryId") Long inventoryId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
