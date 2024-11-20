package com.igym.service;

import com.igym.dto.SaleDTO;
import com.igym.entity.*;
import com.igym.repository.CustomerRepository;
import com.igym.repository.ProductRepository;
import com.igym.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        try {
            Customer customer = customerRepository.findById(saleDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            // Validate stock for all items before processing
            for (SaleDTO.SaleItemDTO itemDTO : saleDTO.getItems()) {
                inventoryService.validateStock(itemDTO.getProductId(), itemDTO.getQuantity());
            }

            Sale sale = new Sale();
            sale.setCustomer(customer);
            sale.setSaleDate(LocalDateTime.now());
            sale.setSubtotal(saleDTO.getSubtotal());
            sale.setTaxRate(saleDTO.getTaxRate());
            sale.setTaxAmount(saleDTO.getTaxAmount());
            sale.setDiscountPercentage(saleDTO.getDiscountPercentage());
            sale.setDiscountAmount(saleDTO.getDiscountAmount());
            sale.setTotal(saleDTO.getTotal());
            sale.setPaymentMethod(saleDTO.getPaymentMethod());

            List<ProductConsumption> items = new ArrayList<>();
            
            // Process each item
            for (SaleDTO.SaleItemDTO itemDTO : saleDTO.getItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                // Update inventory
                inventoryService.processStockForSale(
                    itemDTO.getProductId(),
                    itemDTO.getQuantity(),
                    sale.getId(),
                    1L  // TODO: Get actual employee ID from security context
                );

                // Create consumption record
                ProductConsumption consumption = new ProductConsumption();
                consumption.setSale(sale);
                consumption.setCustomer(customer);
                consumption.setProduct(product);
                consumption.setQuantity(itemDTO.getQuantity());
                consumption.setPricePerUnit(itemDTO.getPricePerUnit());
                consumption.setTotalPrice(itemDTO.getTotalPrice());
                items.add(consumption);
            }

            sale.setItems(items);
            Sale savedSale = saleRepository.save(sale);
            return convertToDTO(savedSale);

        } catch (OptimisticLockException e) {
            throw new RuntimeException("Concurrent stock update detected. Please try again.", e);
        }
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
        return convertToDTO(sale);
    }

    @Override
    public List<SaleDTO> getSalesByCustomerId(Long customerId) {
        return saleRepository.findByCustomerId(customerId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<SaleDTO> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<SaleDTO> getSalesByCustomerAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByCustomerIdAndDateRange(customerId, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Double calculateTotalSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.calculateTotalSalesForPeriod(startDate, endDate);
    }

    @Override
    public Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.calculateTotalSalesForPeriod(startDate, endDate);
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
            
        // Reverse inventory changes
        inventoryService.reverseStockForSale(sale.getId());

        saleRepository.delete(sale);
    }

    @Override
    @Transactional
    public void updateProductStock(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
            
        for (ProductConsumption item : sale.getItems()) {
            inventoryService.processStockForSale(
                item.getProduct().getId(),
                item.getQuantity(),
                saleId,
                1L  // TODO: Get actual employee ID from security context
            );
        }
    }

    private SaleDTO convertToDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setCustomerId(sale.getCustomer().getId());
        dto.setCustomerName(sale.getCustomer().getName());
        dto.setSaleDate(sale.getSaleDate());
        dto.setSubtotal(sale.getSubtotal());
        dto.setTaxRate(sale.getTaxRate());
        dto.setTaxAmount(sale.getTaxAmount());
        dto.setDiscountPercentage(sale.getDiscountPercentage());
        dto.setDiscountAmount(sale.getDiscountAmount());
        dto.setTotal(sale.getTotal());
        dto.setPaymentMethod(sale.getPaymentMethod());

        List<SaleDTO.SaleItemDTO> items = sale.getItems().stream()
            .map(item -> {
                SaleDTO.SaleItemDTO itemDTO = new SaleDTO.SaleItemDTO();
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPricePerUnit(item.getPricePerUnit());
                itemDTO.setTotalPrice(item.getTotalPrice());
                return itemDTO;
            })
            .collect(Collectors.toList());
        
        dto.setItems(items);
        return dto;
    }
}
