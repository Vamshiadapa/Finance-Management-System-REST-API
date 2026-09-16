package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.entity.Account;
import com.finance.entity.Transaction;
import com.finance.entity.enums.TransactionType;
import com.finance.exception.InvalidTransactionException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.AccountRepository;
import com.finance.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public TransactionResponse save(TransactionRequest request) {


        
        if (request.transactionId() != null) {

            Transaction transaction =
                    transactionRepository.findById(
                            request.transactionId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Transaction not found: "
                                            + request.transactionId()
                            ));



            transaction.setTransactionType(
                    request.transactionType()
            );

            transaction.setAmount(
                    request.amount()
            );

            transaction.setTargetAccountId(
                    request.targetAccountId()
            );


            return toResponse(
                    transactionRepository.save(transaction)
            );
        }



        if (request.amount() == null ||
                request.amount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidTransactionException(
                    "Amount must be greater than zero."
            );
        }


        Account source =
                findAccount(request.accountId());


     

        if (request.transactionType() ==
                TransactionType.TRANSFER) {


            if (request.targetAccountId() == null) {

                throw new InvalidTransactionException(
                        "targetAccountId is required for transfer."
                );
            }


            if (source.getAccountId()
                    .equals(request.targetAccountId())) {

                throw new InvalidTransactionException(
                        "Source and target accounts must be different."
                );
            }


            Account target =
                    findAccount(request.targetAccountId());


            checkBalance(
                    source,
                    request.amount()
            );


            source.setBalance(
                    source.getBalance()
                            .subtract(request.amount())
            );


            target.setBalance(
                    target.getBalance()
                            .add(request.amount())
            );


            accountRepository.save(source);

            accountRepository.save(target);
        }



        else if (request.transactionType() ==
                TransactionType.DEPOSIT) {


            source.setBalance(
                    source.getBalance()
                            .add(request.amount())
            );


            accountRepository.save(source);
        }


        

        else if (request.transactionType() ==
                TransactionType.WITHDRAWAL) {


            checkBalance(
                    source,
                    request.amount()
            );


            source.setBalance(
                    source.getBalance()
                            .subtract(request.amount())
            );


            accountRepository.save(source);
        }


    

        Transaction transaction =
                Transaction.builder()

                        .transactionType(
                                request.transactionType()
                        )

                        .amount(
                                request.amount()
                        )

                        .transactionDate(
                                LocalDateTime.now()
                        )

                        .account(source)

                        .targetAccountId(
                                request.targetAccountId()
                        )

                        .build();


        return toResponse(
                transactionRepository.save(transaction)
        );
    }


    
    public Page<TransactionResponse> getAll(int page, int size) {
    	return transactionRepository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }


    public TransactionResponse getById(Long id) {

        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found: "
                                                + id
                                )
                        );

        return toResponse(transaction);
    }


    public void delete(Long id) {

        if (!transactionRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Transaction not found: " + id
            );
        }


        transactionRepository.deleteById(id);
    }



    private Account findAccount(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        )
                );
    }


    private void checkBalance(
            Account account,
            BigDecimal amount) {

        if (account.getBalance()
                .compareTo(amount) < 0) {

            throw new InvalidTransactionException(
                    "Insufficient account balance."
            );
        }
    }


    private TransactionResponse toResponse(
            Transaction transaction) {

        return new TransactionResponse(

                transaction.getTransactionId(),

                transaction.getTransactionType(),

                transaction.getAmount(),

                transaction.getTransactionDate(),

                transaction.getAccount()
                        .getAccountId(),

                transaction.getTargetAccountId()
        );
    }
}