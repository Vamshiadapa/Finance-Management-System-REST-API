package com.finance.dto;
import com.finance.entity.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
public record LoanResponse(Long loanId, String loanType, BigDecimal loanAmount, BigDecimal interestRate,
                           Integer durationMonths, BigDecimal amountRepaid, BigDecimal outstandingAmount,
                           LoanStatus status, LocalDate startDate, LocalDate endDate, Long customerId) {}
