package com.finance.service;

import com.finance.dto.CustomerRequest;
import com.finance.dto.CustomerResponse;
import com.finance.entity.Customer;
import com.finance.repository.CustomerRepository;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomer() {

        CustomerRequest request = new CustomerRequest(
                "Test Customer",
                "test@example.com",
                "9876543210",
                "Hyderabad",
                LocalDate.of(2000, 5, 15)
        );

        Customer savedCustomer = Customer.builder()
                .customerId(1L)
                .customerName("Test Customer")
                .email("test@example.com")
                .phone("9876543210")
                .address("Hyderabad")
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .build();

        when(repository.save(any(Customer.class)))
                .thenReturn(savedCustomer);

        CustomerResponse response = customerService.create(request);

        assertNotNull(response);

        assertEquals(1L, response.customerId());
        assertEquals("Test Customer", response.customerName());
        assertEquals("test@example.com", response.email());
        assertEquals(LocalDate.of(2000, 5, 15), response.dateOfBirth());

        verify(repository).save(any(Customer.class));
    }
}