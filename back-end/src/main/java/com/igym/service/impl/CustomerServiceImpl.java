package com.igym.service.impl;

import com.igym.dto.CustomerDTO;
import com.igym.entity.Customer;
import com.igym.repository.CustomerRepository;
import com.igym.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhotoUrl(customerDTO.getPhotoUrl());
        customer.setBirthDate(customerDTO.getBirthDate());
        customer.setBarcode(customerDTO.getBarcode());
        customer.setBalance(0.0); // Initialize with zero balance
        
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDTO.fromEntity(savedCustomer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByBarcode(String barcode) {
        return customerRepository.existsByBarcode(barcode);
    }

    @Override
    public List<CustomerDTO> searchByName(String searchTerm) {
        return customerRepository.findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(CustomerDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
