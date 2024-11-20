package com.igym.repository;

import com.igym.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    Optional<Inventory> findByProductId(Long productId);
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId")
    Optional<Inventory> findByProductIdWithLock(@Param("productId") Long productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityInStock <= :threshold")
    List<Inventory> findLowStockItems(@Param("threshold") Integer threshold);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityInStock = 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT CASE WHEN i.quantityInStock >= :quantity THEN true ELSE false END FROM Inventory i WHERE i.product.id = :productId")
    boolean hasEnoughStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
