package com.finance.service;

import com.finance.dto.*;
import com.finance.entity.*;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountResponse create(AccountRequest r) {
        Customer c = customerRepository.findById(r.customerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + r.customerId()));
        Account a = Account.builder()
            .accountNumber(generateAccountNumber())
            .accountType(r.accountType()).balance(BigDecimal.ZERO).customer(c).build();
        return toResponse(accountRepository.save(a));
    }
    public List<AccountResponse> getAll() { return accountRepository.findAll().stream().map(this::toResponse).toList(); }
    public AccountResponse getById(Long id) { return toResponse(find(id)); }
    public AccountResponse update(Long id, AccountRequest r) {
        Account a = find(id);
        Customer c = customerRepository.findById(r.customerId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + r.customerId()));
        a.setAccountType(r.accountType()); a.setCustomer(c);
        return toResponse(accountRepository.save(a));
    }
    public void delete(Long id) { accountRepository.delete(find(id)); }
    public Account find(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    private AccountResponse toResponse(Account a) {
        return new AccountResponse(a.getAccountId(), a.getAccountNumber(), a.getAccountType(), a.getBalance(),
                a.getCustomer().getCustomerId());
    }
}
