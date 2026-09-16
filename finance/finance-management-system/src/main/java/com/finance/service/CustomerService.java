package com.finance.service;

import com.finance.dto.CustomerRequest;
import com.finance.dto.CustomerResponse;
import com.finance.entity.Customer;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;


    public CustomerResponse save(CustomerRequest request) {

        Customer customer;

     
        if (request.customerId() == null) {

            customer = new Customer();

        }

       
        else {

            customer = repository.findById(request.customerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Customer not found: "
                                            + request.customerId()));
        }


      

        customer.setCustomerName(request.customerName());

        customer.setEmail(request.email());

        customer.setPhone(request.phone());

        customer.setAddress(request.address());

        customer.setDateOfBirth(request.dateOfBirth());


      
        Customer savedCustomer = repository.save(customer);



        return toResponse(savedCustomer);
    }


  
    public Page<CustomerResponse> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }
    


    public CustomerResponse getById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + id));

        return toResponse(customer);
    }


    
    public void delete(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + id));

        repository.delete(customer);
    }


    
    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getDateOfBirth()
        );
    }
}