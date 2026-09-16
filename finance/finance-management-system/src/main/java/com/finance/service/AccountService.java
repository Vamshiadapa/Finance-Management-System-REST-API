package com.finance.service;

import com.finance.dto.AccountRequest;
import com.finance.dto.AccountResponse;
import com.finance.entity.Account;
import com.finance.entity.Customer;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.AccountRepository;
import com.finance.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;



    public AccountResponse save(AccountRequest request) {

        // Find customer
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: "
                                        + request.customerId()
                        ));


        Account account;


     
        if (request.accountId() == null) {

            account = Account.builder()
                    .accountNumber(generateAccountNumber())
                    .accountType(request.accountType())
                    .balance(BigDecimal.ZERO)
                    .customer(customer)
                    .build();

        }

      
        else {

            account = accountRepository.findById(request.accountId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Account not found: "
                                            + request.accountId()
                            ));

            account.setAccountType(request.accountType());
            account.setCustomer(customer);
        }



        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }



    public Page<AccountResponse> getAll(int page, int size) {
    	return accountRepository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }



    public AccountResponse getById(Long id) {

        return toResponse(find(id));
    }


    
    public void delete(Long id) {

        accountRepository.delete(find(id));
    }


 
    public Account find(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));
    }


    private String generateAccountNumber() {

        return "ACC"
                + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }


    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCustomer().getCustomerId()
        );
    }
}