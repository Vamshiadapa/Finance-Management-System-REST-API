package com.finance.dto;

import com.finance.entity.enums.AccountType;

import jakarta.validation.constraints.NotNull;

public record AccountRequest(

        Long accountId,

        @NotNull
        AccountType accountType,

        @NotNull
        Long customerId

) {
}