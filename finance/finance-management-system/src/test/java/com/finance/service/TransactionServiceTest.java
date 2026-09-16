package com.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.entity.Account;
import com.finance.entity.Transaction;
import com.finance.entity.enums.TransactionType;
import com.finance.repository.AccountRepository;
import com.finance.repository.TransactionRepository;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {


    @Mock
    private TransactionRepository transactionRepository;


    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    private TransactionService transactionService;


    // =====================================================
    // CREATE - DEPOSIT
    // =====================================================
    @Test
    void depositAddsMoneyToAccount() {


        Account account = new Account();

        account.setAccountId(1L);

        account.setBalance(
                new BigDecimal("1000.00")
        );


        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        Transaction savedTransaction =
                new Transaction();

        savedTransaction.setTransactionId(1L);

        savedTransaction.setTransactionType(
                TransactionType.DEPOSIT
        );

        savedTransaction.setAmount(
                new BigDecimal("500.00")
        );

        savedTransaction.setAccount(account);


        when(transactionRepository.save(
                any(Transaction.class)))
                .thenReturn(savedTransaction);


        TransactionRequest request =
                new TransactionRequest(

                        null,

                        TransactionType.DEPOSIT,

                        new BigDecimal("500.00"),

                        1L,

                        null
                );


        TransactionResponse response =
                transactionService.save(request);


        assertNotNull(response);


        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance()
        );


        verify(accountRepository)
                .save(account);


        verify(transactionRepository)
                .save(any(Transaction.class));
    }


    // =====================================================
    // CREATE - WITHDRAWAL
    // =====================================================
    @Test
    void withdrawalWithInsufficientBalanceThrowsException() {


        Account account = new Account();

        account.setAccountId(1L);

        account.setBalance(
                new BigDecimal("100.00")
        );


        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));


        TransactionRequest request =
                new TransactionRequest(

                        null,

                        TransactionType.WITHDRAWAL,

                        new BigDecimal("500.00"),

                        1L,

                        null
                );


        assertThrows(

                RuntimeException.class,

                () -> transactionService.save(request)
        );


        verify(
                transactionRepository,
                never()
        ).save(any(Transaction.class));
    }


    // =====================================================
    // UPDATE TRANSACTION
    // =====================================================
    @Test
    void updateTransactionSuccessfully() {


        Account account = new Account();

        account.setAccountId(1L);

        account.setBalance(
                new BigDecimal("1500.00")
        );


        Transaction existingTransaction =
                new Transaction();

        existingTransaction.setTransactionId(1L);

        existingTransaction.setTransactionType(
                TransactionType.DEPOSIT
        );

        existingTransaction.setAmount(
                new BigDecimal("500.00")
        );

        existingTransaction.setAccount(account);


        when(transactionRepository.findById(1L))
                .thenReturn(
                        Optional.of(existingTransaction)
                );


        when(transactionRepository.save(
                any(Transaction.class)))
                .thenReturn(existingTransaction);


        TransactionRequest request =
                new TransactionRequest(

                        1L,

                        TransactionType.DEPOSIT,

                        new BigDecimal("600.00"),

                        1L,

                        null
                );


        TransactionResponse response =
                transactionService.save(request);


        assertNotNull(response);


        assertEquals(
                1L,
                response.transactionId()
        );


        assertEquals(
                TransactionType.DEPOSIT,
                response.transactionType()
        );


        assertEquals(
                new BigDecimal("600.00"),
                response.amount()
        );


        // Balance should NOT change during transaction update
        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance()
        );


        verify(transactionRepository)
                .findById(1L);


        verify(transactionRepository)
                .save(existingTransaction);
    }
}