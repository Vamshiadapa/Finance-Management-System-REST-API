package com.finance.service;

import com.finance.dto.*;
import com.finance.entity.Customer;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerResponse create(CustomerRequest r) {
        Customer c = Customer.builder()
                .customerName(r.customerName())
                .email(r.email())
                .phone(r.phone())
                .address(r.address())
                .dateOfBirth(r.dateOfBirth())  
                .build();

        return toResponse(repository.save(c));
    }

    public List<CustomerResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse getById(Long id) {
        return toResponse(find(id));
    }

    public CustomerResponse update(Long id, CustomerRequest r) {
        Customer c = find(id);

        c.setCustomerName(r.customerName());
        c.setEmail(r.email());
        c.setPhone(r.phone());
        c.setAddress(r.address());
        c.setDateOfBirth(r.dateOfBirth()); 

        return toResponse(repository.save(c));
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Customer find(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Customer not found: " + id));
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(
                c.getCustomerId(),
                c.getCustomerName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getDateOfBirth()
        );
    }
}