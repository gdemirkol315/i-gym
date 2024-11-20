package com.igym.service;

import com.igym.dto.SaleDTO;
import com.igym.entity.Customer;
import com.igym.entity.Product;
import com.igym.entity.ProductConsumption;
import com.igym.entity.Sale;
import com.igym.repository.CustomerRepository;
import com.igym.repository.ProductRepository;
import com.igym.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        Customer customer = customerRepository.findById(saleDTO.getCustomerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

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

        // Create ProductConsumption entries for each item
        List<ProductConsumption> items = saleDTO.getItems().stream()
            .map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                
                // Update product stock
                int newStock = product.getQuantityInStock() - itemDTO.getQuantity();
                if (newStock < 0) {
                    throw new IllegalStateException("Insufficient stock for product: " + product.getName());
                }
                product.setQuantityInStock(newStock);
                productRepository.save(product);

                // Create consumption record
                ProductConsumption consumption = new ProductConsumption();
                consumption.setSale(sale);
                consumption.setCustomer(customer);
                consumption.setProduct(product);
                consumption.setQuantity(itemDTO.getQuantity());
                consumption.setPricePerUnit(itemDTO.getPricePerUnit());
                consumption.setTotalPrice(itemDTO.getTotalPrice());
                return consumption;
            })
            .collect(Collectors.toList());

        sale.setItems(items);
        Sale savedSale = saleRepository.save(sale);
        return convertToDTO(savedSale);
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
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
            
        // Restore product quantities
        sale.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantityInStock(product.getQuantityInStock() + item.getQuantity());
            productRepository.save(product);
        });

        saleRepository.delete(sale);
    }

    @Override
    public Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.calculateTotalSalesForPeriod(startDate, endDate);
    }

    @Override
    @Transactional
    public void updateProductStock(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
            
        sale.getItems().forEach(item -> {
            Product product = item.getProduct();
            int newStock = product.getQuantityInStock() - item.getQuantity();
            if (newStock < 0) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setQuantityInStock(newStock);
            productRepository.save(product);
        });
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
