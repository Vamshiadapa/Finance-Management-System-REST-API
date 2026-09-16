package com.finance.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public record InvestmentResponse(Long investmentId, String investmentType, BigDecimal amount,
                                  LocalDate maturityDate, BigDecimal currentValue,
                                  String performanceInfo, Long customerId) {}
