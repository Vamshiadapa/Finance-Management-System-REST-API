package com.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequest(
        Long loanId,
        @NotBlank String loanType,
        @NotNull @Positive BigDecimal loanAmount,
        @NotNull @Positive BigDecimal interestRate,
        @NotNull @Positive Integer durationMonths,
        @NotNull Long customerId,
        LocalDate startDate
) {
}