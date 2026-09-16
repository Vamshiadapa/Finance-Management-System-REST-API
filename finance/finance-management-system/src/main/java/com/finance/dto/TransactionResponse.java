package com.finance.dto;

import com.finance.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long transactionId,

        TransactionType transactionType,

        BigDecimal amount,

        LocalDateTime transactionDate,

        Long accountId,

        Long targetAccountId

) {
}