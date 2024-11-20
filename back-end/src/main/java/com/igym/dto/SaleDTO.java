package com.igym.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDateTime saleDate;
    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double discountPercentage;
    private Double discountAmount;
    private Double total;
    private String paymentMethod;
    private List<SaleItemDTO> items = new ArrayList<>();

    @Data
    public static class SaleItemDTO {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double pricePerUnit;
        private Double totalPrice;
    }
}
