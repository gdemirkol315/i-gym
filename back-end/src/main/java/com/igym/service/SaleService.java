package com.igym.service;

import com.igym.dto.SaleDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    SaleDTO createSale(SaleDTO saleDTO);
    
    SaleDTO getSaleById(Long id);
    
    List<SaleDTO> getSalesByCustomerId(Long customerId);
    
    List<SaleDTO> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<SaleDTO> getSalesByCustomerAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    
    Double calculateTotalSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    void deleteSale(Long id);
    
    // Additional business methods
    Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);
    
    void updateProductStock(Long saleId);
}
