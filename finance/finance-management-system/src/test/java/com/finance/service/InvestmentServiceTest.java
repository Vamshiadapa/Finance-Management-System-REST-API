package com.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.dto.InvestmentRequest;
import com.finance.dto.InvestmentResponse;
import com.finance.entity.Customer;
import com.finance.entity.Investment;
import com.finance.repository.CustomerRepository;
import com.finance.repository.InvestmentRepository;

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
        customer.setCustomerId(2L);
        customer.setCustomerName("Vamshi");

        when(customerRepository.findById(2L))
                .thenReturn(Optional.of(customer));

        Investment savedInvestment = Investment.builder()
                .investmentId(10L)
                .investmentType("FD")
                .amount(new BigDecimal("50000"))
                .currentValue(new BigDecimal("50000"))
                .investedDate(LocalDate.of(2026, 9, 16))
                .maturityDate(LocalDate.of(2027, 9, 16))
                .status("ACTIVE")
                .performanceInfo("FD investment")
                .customer(customer)
                .build();

        when(repository.save(any(Investment.class)))
                .thenReturn(savedInvestment);

        InvestmentRequest request = new InvestmentRequest(
                null,
                "FD",
                new BigDecimal("50000"),
                LocalDate.of(2026, 9, 16),
                LocalDate.of(2027, 9, 16),
                new BigDecimal("50000"),
                "FD investment",
                "ACTIVE",
                2L
        );

        InvestmentResponse result =
                investmentService.save(request);

        assertNotNull(result);
        assertEquals(10L, result.investmentId());
        assertEquals("FD", result.investmentType());
        assertEquals(new BigDecimal("50000"), result.amount());
        assertEquals(new BigDecimal("50000"), result.currentValue());
        assertEquals(2L, result.customerId());

        verify(customerRepository).findById(2L);
        verify(repository).save(any(Investment.class));
    }

    @Test
    void updateInvestmentSuccessfully() {

        Customer customer = new Customer();
        customer.setCustomerId(2L);
        customer.setCustomerName("Vamshi");

        Investment existingInvestment = Investment.builder()
                .investmentId(10L)
                .investmentType("FD")
                .amount(new BigDecimal("50000"))
                .currentValue(new BigDecimal("50000"))
                .investedDate(LocalDate.of(2026, 9, 16))
                .maturityDate(LocalDate.of(2027, 9, 16))
                .status("ACTIVE")
                .customer(customer)
                .build();

        when(customerRepository.findById(2L))
                .thenReturn(Optional.of(customer));

        when(repository.findById(10L))
                .thenReturn(Optional.of(existingInvestment));

        when(repository.save(any(Investment.class)))
                .thenReturn(existingInvestment);

        InvestmentRequest request = new InvestmentRequest(
                10L,
                "FD",
                new BigDecimal("60000"),
                LocalDate.of(2026, 9, 16),
                LocalDate.of(2027, 9, 16),
                new BigDecimal("62000"),
                "Updated FD investment",
                "ACTIVE",
                2L
        );

        InvestmentResponse result =
                investmentService.save(request);

        assertNotNull(result);
        assertEquals(10L, result.investmentId());
        assertEquals("FD", result.investmentType());
        assertEquals(new BigDecimal("60000"), result.amount());
        assertEquals(new BigDecimal("62000"), result.currentValue());
        assertEquals(2L, result.customerId());

        verify(customerRepository).findById(2L);
        verify(repository).findById(10L);
        verify(repository).save(existingInvestment);
    }
}