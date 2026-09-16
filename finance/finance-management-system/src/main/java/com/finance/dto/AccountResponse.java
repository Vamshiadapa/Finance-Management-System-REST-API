package com.finance.dto;
import com.finance.entity.enums.AccountType;
import java.math.BigDecimal;
public record AccountResponse(Long accountId, String accountNumber, AccountType accountType, BigDecimal balance, Long customerId) {}
