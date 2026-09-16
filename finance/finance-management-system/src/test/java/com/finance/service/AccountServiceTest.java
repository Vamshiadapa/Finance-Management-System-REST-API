package com.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.dto.AccountRequest;
import com.finance.dto.AccountResponse;
import com.finance.entity.Account;
import com.finance.entity.Customer;
import com.finance.entity.enums.AccountType;
import com.finance.repository.AccountRepository;
import com.finance.repository.CustomerRepository;


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

        Customer customer = new Customer();

        customer.setCustomerId(1L);

        customer.setCustomerName("Vamshi");


        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));


        Account savedAccount = Account.builder()
                .accountId(10L)
                .accountNumber("ACC123456789")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .customer(customer)
                .build();


        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);


        AccountRequest request =
                new AccountRequest(
                        null,
                        AccountType.SAVINGS,
                        1L
                );


        AccountResponse result =
                accountService.save(request);


        assertNotNull(result);

        assertEquals(
                10L,
                result.accountId()
        );

        assertEquals(
                "ACC123456789",
                result.accountNumber()
        );

        assertEquals(
                AccountType.SAVINGS,
                result.accountType()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.balance()
        );

        assertEquals(
                1L,
                result.customerId()
        );


        verify(customerRepository)
                .findById(1L);

        verify(accountRepository)
                .save(any(Account.class));
    }


    @Test
    void updateAccountSuccessfully() {

        Customer customer = new Customer();

        customer.setCustomerId(1L);

        customer.setCustomerName("Vamshi");


        Account existingAccount = Account.builder()
                .accountId(10L)
                .accountNumber("ACC123456789")
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("8000"))
                .customer(customer)
                .build();


        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));


        when(accountRepository.findById(10L))
                .thenReturn(Optional.of(existingAccount));


        when(accountRepository.save(any(Account.class)))
                .thenReturn(existingAccount);


        AccountRequest request =
                new AccountRequest(
                        10L,
                        AccountType.CURRENT,
                        1L
                );


        AccountResponse result =
                accountService.save(request);


        assertNotNull(result);

        assertEquals(
                10L,
                result.accountId()
        );

        assertEquals(
                "ACC123456789",
                result.accountNumber()
        );

        assertEquals(
                AccountType.CURRENT,
                result.accountType()
        );

        assertEquals(
                new BigDecimal("8000"),
                result.balance()
        );

        assertEquals(
                1L,
                result.customerId()
        );


        verify(customerRepository)
                .findById(1L);

        verify(accountRepository)
                .findById(10L);

        verify(accountRepository)
                .save(any(Account.class));
    }
}