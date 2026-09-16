package com.finance.service;

import com.finance.dto.InvestmentRequest;
import com.finance.dto.InvestmentResponse;
import com.finance.entity.Customer;
import com.finance.entity.Investment;
import com.finance.repository.CustomerRepository;
import com.finance.repository.InvestmentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private InvestmentService investmentService;

    @Test
    void createInvestmentSuccessfully() {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Ravi");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Investment savedInvestment = Investment.builder()
                .investmentId(10L)
                .investmentType("STOCKS")
                .amount(new BigDecimal("10000.00"))
                .investedDate(LocalDate.of(2026, 9, 16))
                .maturityDate(LocalDate.of(2027, 9, 15))
                .currentValue(new BigDecimal("11000.00"))
                .performanceInfo("Good performance")
                .status("ACTIVE")
                .customer(customer)
                .build();

        when(repository.save(any(Investment.class)))
                .thenReturn(savedInvestment);

        InvestmentRequest request = new InvestmentRequest(
                "STOCKS",
                new BigDecimal("10000.00"),
                LocalDate.of(2026, 9, 16),
                LocalDate.of(2027, 9, 15),
                new BigDecimal("11000.00"),
                "Good performance",
                "ACTIVE",
                1L
        );

        InvestmentResponse result =
                investmentService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.investmentId());
        assertEquals("STOCKS", result.investmentType());
        assertEquals(new BigDecimal("10000.00"), result.amount());
        assertEquals(new BigDecimal("11000.00"), result.currentValue());
        assertEquals("Good performance", result.performanceInfo());
        assertEquals(1L, result.customerId());

        verify(customerRepository).findById(1L);
        verify(repository).save(any(Investment.class));
    }
}