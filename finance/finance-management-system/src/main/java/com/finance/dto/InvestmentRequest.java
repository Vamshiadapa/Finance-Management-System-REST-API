package com.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentRequest(

    @NotBlank
    String investmentType,

    @NotNull
    @Positive
    BigDecimal amount,

    @NotNull
    LocalDate investedDate,

    @NotNull
    LocalDate maturityDate,

    BigDecimal currentValue,

    String performanceInfo,

    String status,

    @NotNull
    Long customerId

) {}