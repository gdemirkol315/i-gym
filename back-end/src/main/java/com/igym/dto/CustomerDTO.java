package com.igym.dto;

import com.igym.entity.Customer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String photoUrl;
    private LocalDateTime birthDate;
    private String qrCode;
    private Double balance;
    private LocalDateTime createdAt;

    public static CustomerDTO fromEntity(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhotoUrl(customer.getPhotoUrl());
        dto.setBirthDate(customer.getBirthDate());
        dto.setQrCode(customer.getQrCode());
        dto.setBalance(customer.getBalance());
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
}
