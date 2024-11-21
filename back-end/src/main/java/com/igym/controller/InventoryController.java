package com.igym.controller;

import com.igym.dto.AddStockRequest;
import com.igym.entity.InventoryTransaction;
import com.igym.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add-stock")
    public ResponseEntity<Void> addStock(@RequestBody AddStockRequest request) {
        inventoryService.addStock(
            request.getProductId(),
            request.getQuantity(),
            "MANUAL_ADD",
            request.getNotes(),
            request.getEmployeeId()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/adjust")
    public ResponseEntity<Void> adjustStock(
            @PathVariable Long productId,
            @RequestParam Integer newQuantity,
            @RequestParam(required = false) String notes,
            @RequestParam Long employeeId) {
        inventoryService.adjustStock(productId, newQuantity, notes, employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/transactions")
    public ResponseEntity<List<InventoryTransaction>> getTransactionHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getTransactionHistory(productId));
    }

    @GetMapping("/{productId}/transactions/date-range")
    public ResponseEntity<List<InventoryTransaction>> getTransactionsByDateRange(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(inventoryService.getTransactionsByDateRange(productId, startDate, endDate));
    }

    @GetMapping("/{productId}/stock-movement")
    public ResponseEntity<Integer> getStockMovement(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(inventoryService.calculateStockMovement(productId, startDate, endDate));
    }

    @GetMapping("/{productId}/check-stock")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.hasEnoughStock(productId, quantity));
    }
}
