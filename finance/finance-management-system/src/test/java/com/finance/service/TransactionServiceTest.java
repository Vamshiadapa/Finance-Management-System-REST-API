package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.entity.Account;
import com.finance.entity.Transaction;
import com.finance.entity.enums.TransactionType;
import com.finance.repository.AccountRepository;
import com.finance.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void depositAddsMoneyToAccount() {

        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("1000.00"));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionId(1L);
        savedTransaction.setTransactionType(TransactionType.DEPOSIT);
        savedTransaction.setAmount(new BigDecimal("500.00"));
        savedTransaction.setAccount(account);

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionRequest request = new TransactionRequest(
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                1L,
                null
        );

        TransactionResponse response =
                transactionService.create(request);

        assertNotNull(response);
        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdrawalWithInsufficientBalanceThrowsException() {

        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        TransactionRequest request = new TransactionRequest(
                TransactionType.WITHDRAWAL,
                new BigDecimal("500.00"),
                1L,
                null
        );

        assertThrows(
                RuntimeException.class,
                () -> transactionService.create(request)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }
}