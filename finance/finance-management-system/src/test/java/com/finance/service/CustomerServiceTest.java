package com.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.dto.CustomerRequest;
import com.finance.dto.CustomerResponse;
import com.finance.entity.Customer;
import com.finance.repository.CustomerRepository;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    @Mock
    private CustomerRepository repository;


    @InjectMocks
    private CustomerService customerService;


    // =====================================================
    // CREATE CUSTOMER
    // =====================================================
    @Test
    void testCreateCustomer() {

        CustomerRequest request = new CustomerRequest(

                null,

                "Ravi Kumar",

                "ravi@gmail.com",

                "9876543210",

                "Hyderabad",

                LocalDate.of(1999, 8, 20)
        );


        Customer customer = new Customer();

        customer.setCustomerId(5L);

        customer.setCustomerName("Ravi Kumar");

        customer.setEmail("ravi@gmail.com");

        customer.setPhone("9876543210");

        customer.setAddress("Hyderabad");

        customer.setDateOfBirth(
                LocalDate.of(1999, 8, 20)
        );


        when(repository.save(any(Customer.class)))
                .thenReturn(customer);


        CustomerResponse response =
                customerService.save(request);


        assertNotNull(response);


        assertEquals(
                5L,
                response.customerId()
        );


        assertEquals(
                "Ravi Kumar",
                response.customerName()
        );


        assertEquals(
                "ravi@gmail.com",
                response.email()
        );


        assertEquals(
                "9876543210",
                response.phone()
        );


        assertEquals(
                "Hyderabad",
                response.address()
        );


        assertEquals(
                LocalDate.of(1999, 8, 20),
                response.dateOfBirth()
        );
    }


    // =====================================================
    // UPDATE CUSTOMER
    // =====================================================
    @Test
    void testUpdateCustomer() {

        CustomerRequest request = new CustomerRequest(

                2L,

                "Ravi Kumar Updated",

                "ravi.updated@gmail.com",

                "9876543211",

                "Hyderabad Updated",

                LocalDate.of(1999, 8, 20)
        );


        Customer existingCustomer = new Customer();

        existingCustomer.setCustomerId(2L);

        existingCustomer.setCustomerName("Ravi Kumar");

        existingCustomer.setEmail("ravi@gmail.com");

        existingCustomer.setPhone("9876543210");

        existingCustomer.setAddress("Hyderabad");

        existingCustomer.setDateOfBirth(
                LocalDate.of(1999, 8, 20)
        );


        when(repository.findById(2L))
                .thenReturn(Optional.of(existingCustomer));


        when(repository.save(any(Customer.class)))
                .thenReturn(existingCustomer);


        CustomerResponse response =
                customerService.save(request);


        assertNotNull(response);


        assertEquals(
                2L,
                response.customerId()
        );


        assertEquals(
                "Ravi Kumar Updated",
                response.customerName()
        );


        assertEquals(
                "ravi.updated@gmail.com",
                response.email()
        );


        assertEquals(
                "9876543211",
                response.phone()
        );


        assertEquals(
                "Hyderabad Updated",
                response.address()
        );
    }
}