package com.igym.service;

import com.igym.dto.CustomerDTO;
import com.igym.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
}
