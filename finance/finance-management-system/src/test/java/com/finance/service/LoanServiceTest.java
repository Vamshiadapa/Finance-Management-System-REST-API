package com.finance.service;

import com.finance.dto.LoanResponse;
import com.finance.dto.RepaymentRequest;
import com.finance.entity.Customer;
import com.finance.entity.Loan;
import com.finance.entity.enums.LoanStatus;
import com.finance.repository.CustomerRepository;
import com.finance.repository.LoanRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void repaymentClosesLoanWhenFullyPaid() {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Ravi");

        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanType("HOME_LOAN");
        loan.setLoanAmount(new BigDecimal("10000.00"));
        loan.setInterestRate(new BigDecimal("8.50"));
        loan.setDurationMonths(12);
        loan.setAmountRepaid(new BigDecimal("1000.00"));
        loan.setOutstandingAmount(new BigDecimal("1000.00"));
        loan.setStatus(LoanStatus.APPROVED);
        loan.setCustomer(customer);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        when(loanRepository.save(any(Loan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RepaymentRequest request =
                new RepaymentRequest(new BigDecimal("1000.00"));

        LoanResponse result =
                loanService.repay(1L, request);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("2000.00"),
                result.amountRepaid()
        );

        assertEquals(
                0,
                result.outstandingAmount().compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                LoanStatus.CLOSED,
                result.status()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                loan.getAmountRepaid()
        );

        assertEquals(
                0,
                loan.getOutstandingAmount().compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                LoanStatus.CLOSED,
                loan.getStatus()
        );

        verify(loanRepository).findById(1L);
        verify(loanRepository).save(loan);
    }

    @Test
    void repaymentCannotExceedOutstandingAmount() {

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanAmount(new BigDecimal("10000.00"));
        loan.setInterestRate(new BigDecimal("8.50"));
        loan.setDurationMonths(12);
        loan.setAmountRepaid(new BigDecimal("1000.00"));
        loan.setOutstandingAmount(new BigDecimal("1000.00"));
        loan.setStatus(LoanStatus.APPROVED);
        loan.setCustomer(customer);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        RepaymentRequest request =
                new RepaymentRequest(new BigDecimal("1500.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> loanService.repay(1L, request)
        );

        verify(loanRepository, never())
                .save(any(Loan.class));
    }
}