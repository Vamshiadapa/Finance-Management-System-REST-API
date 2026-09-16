package com.finance.service;

import com.finance.dto.*;
import com.finance.entity.*;
import com.finance.entity.enums.TransactionType;
import com.finance.exception.InvalidTransactionException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponse create(TransactionRequest r) {
        if (r.amount() == null || r.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException("Amount must be greater than zero.");

        Account source = findAccount(r.accountId());

        if (r.transactionType() == TransactionType.TRANSFER) {
            if (r.targetAccountId() == null)
                throw new InvalidTransactionException("targetAccountId is required for transfer.");
            if (source.getAccountId().equals(r.targetAccountId()))
                throw new InvalidTransactionException("Source and target accounts must be different.");

            Account target = findAccount(r.targetAccountId());
            checkBalance(source, r.amount());
            source.setBalance(source.getBalance().subtract(r.amount()));
            target.setBalance(target.getBalance().add(r.amount()));
            accountRepository.save(source);
            accountRepository.save(target);
        } else if (r.transactionType() == TransactionType.DEPOSIT) {
            source.setBalance(source.getBalance().add(r.amount()));
            accountRepository.save(source);
        } else if (r.transactionType() == TransactionType.WITHDRAWAL) {
            checkBalance(source, r.amount());
            source.setBalance(source.getBalance().subtract(r.amount()));
            accountRepository.save(source);
        }

        Transaction tx = Transaction.builder()
                .transactionType(r.transactionType()).amount(r.amount())
                .transactionDate(LocalDateTime.now()).account(source)
                .targetAccountId(r.targetAccountId()).build();
        return toResponse(transactionRepository.save(tx));
    }

    public List<TransactionResponse> getAll() { return transactionRepository.findAll().stream().map(this::toResponse).toList(); }
    public TransactionResponse getById(Long id) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        return toResponse(t);
    }
    public TransactionResponse update(Long id, TransactionRequest r) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        // Financial transaction history should normally be immutable.
        // This endpoint is retained to match the required CRUD structure.
        t.setTransactionType(r.transactionType());
        t.setAmount(r.amount());
        t.setTargetAccountId(r.targetAccountId());
        return toResponse(transactionRepository.save(t));
    }
    public void delete(Long id) {
        if (!transactionRepository.existsById(id))
            throw new ResourceNotFoundException("Transaction not found: " + id);
        transactionRepository.deleteById(id);
    }
    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }
    private void checkBalance(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0)
            throw new InvalidTransactionException("Insufficient account balance.");
    }
    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getTransactionId(), t.getTransactionType(), t.getAmount(),
                t.getTransactionDate(), t.getAccount().getAccountId(), t.getTargetAccountId());
    }
}
