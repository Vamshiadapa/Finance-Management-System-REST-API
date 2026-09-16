package com.finance.service;

import com.finance.dto.AccountRequest;
import com.finance.dto.AccountResponse;
import com.finance.entity.Account;
import com.finance.entity.Customer;
import com.finance.entity.enums.AccountType;
import com.finance.repository.AccountRepository;
import com.finance.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccessfully() {

        // Customer
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Vamshi");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // Saved Account
        Account savedAccount = Account.builder()
                .accountId(10L)
                .accountNumber("ACC123456789")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .customer(customer)
                .build();

        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        // Request
        AccountRequest request =
                new AccountRequest(AccountType.SAVINGS, 1L);

        // Call service
        AccountResponse result =
                accountService.create(request);

        // Assertions
        assertNotNull(result);
        assertEquals(10L, result.accountId());
        assertEquals("ACC123456789", result.accountNumber());
        assertEquals(AccountType.SAVINGS, result.accountType());
        assertEquals(BigDecimal.ZERO, result.balance());
        assertEquals(1L, result.customerId());

        // Verify repository calls
        verify(customerRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }
}