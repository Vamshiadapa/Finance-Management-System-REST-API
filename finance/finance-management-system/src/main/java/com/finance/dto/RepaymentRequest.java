package com.finance.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
public record RepaymentRequest(@NotNull @Positive BigDecimal amount) {}
