package com.igym.controller;

import com.igym.dto.CustomerDTO;
import com.igym.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check-barcode/{barcode}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('SUPERVISOR')")
    public ResponseEntity<Boolean> checkBarcodeExists(@PathVariable String barcode) {
        try {
            boolean exists = customerService.existsByBarcode(barcode);
            return ResponseEntity.ok(exists);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('SUPERVISOR')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String term) {
        try {
            List<CustomerDTO> customers = customerService.searchByName(term);
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('SUPERVISOR')")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        try {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setName(request.getName());
            customerDTO.setLastName(request.getLastName());
            customerDTO.setBarcode(request.getBarcode());
            customerDTO.setEmail(request.getEmail());
            customerDTO.setPhotoUrl(request.getPhotoUrl());
            customerDTO.setBirthDate(request.getBirthDate());
            
            CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
            return ResponseEntity.ok(createdCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
    }

    @Data
    public static class CreateCustomerRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        private String photoUrl;

        private LocalDateTime birthDate;
        private String barcode;
    }
}
