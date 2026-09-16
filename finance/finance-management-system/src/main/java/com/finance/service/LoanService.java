package com.finance.service;

import com.finance.dto.LoanRequest;
import com.finance.dto.LoanResponse;
import com.finance.dto.RepaymentRequest;
import com.finance.entity.Customer;
import com.finance.entity.Loan;
import com.finance.entity.enums.LoanStatus;
import com.finance.exception.InvalidTransactionException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.CustomerRepository;
import com.finance.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public LoanResponse save(LoanRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + request.customerId()));

        if (customer.getDateOfBirth() == null) {
            throw new InvalidTransactionException(
                    "Date of birth is required to apply for a loan");
        }

        int age = Period.between(
                customer.getDateOfBirth(),
                LocalDate.now()
        ).getYears();

        if (age < 18) {
            throw new InvalidTransactionException(
                    "Customer must be 18 years or older to apply for a loan");
        }

        Loan loan;

        if (request.loanId() == null) {

            BigDecimal interest = request.loanAmount()
                    .multiply(request.interestRate())
                    .multiply(BigDecimal.valueOf(request.durationMonths()))
                    .divide(BigDecimal.valueOf(1200));

            BigDecimal totalPayable =
                    request.loanAmount().add(interest);

            loan = Loan.builder()
                    .loanType(request.loanType())
                    .loanAmount(request.loanAmount())
                    .interestRate(request.interestRate())
                    .durationMonths(request.durationMonths())
                    .totalPayable(totalPayable)
                    .amountRepaid(BigDecimal.ZERO)
                    .outstandingAmount(totalPayable)
                    .status(LoanStatus.PENDING)
                    .startDate(request.startDate() == null
                            ? LocalDate.now()
                            : request.startDate())
                    .customer(customer)
                    .build();

        } else {

            loan = loanRepository.findById(request.loanId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Loan not found: " + request.loanId()));

            BigDecimal interest = request.loanAmount()
                    .multiply(request.interestRate())
                    .multiply(BigDecimal.valueOf(request.durationMonths()))
                    .divide(BigDecimal.valueOf(1200));

            BigDecimal totalPayable =
                    request.loanAmount().add(interest);

            loan.setLoanType(request.loanType());
            loan.setLoanAmount(request.loanAmount());
            loan.setInterestRate(request.interestRate());
            loan.setDurationMonths(request.durationMonths());
            loan.setTotalPayable(totalPayable);
            loan.setCustomer(customer);

            BigDecimal amountRepaid =
                    loan.getAmountRepaid() == null
                            ? BigDecimal.ZERO
                            : loan.getAmountRepaid();

            loan.setOutstandingAmount(
                    totalPayable.subtract(amountRepaid)
                            .max(BigDecimal.ZERO)
            );

            if (request.startDate() != null) {
                loan.setStartDate(request.startDate());
            }
        }

        return toResponse(loanRepository.save(loan));
    }

    public Page<LoanResponse> getAll(int page, int size) {
        return loanRepository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    public LoanResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public LoanResponse approve(Long id) {

        Loan loan = find(id);

        loan.setStatus(LoanStatus.APPROVED);
        loan.setStartDate(LocalDate.now());
        loan.setEndDate(
                LocalDate.now().plusMonths(loan.getDurationMonths())
        );

        return toResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse repay(Long id, RepaymentRequest request) {

        Loan loan = find(id);

        if (loan.getStatus() != LoanStatus.APPROVED &&
                loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Loan must be approved or active before repayment.");
        }

        if (request.amount().compareTo(
                loan.getOutstandingAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Repayment cannot exceed outstanding amount.");
        }

        loan.setAmountRepaid(
                loan.getAmountRepaid().add(request.amount())
        );

        loan.setOutstandingAmount(
                loan.getOutstandingAmount()
                        .subtract(request.amount())
        );

        if (loan.getOutstandingAmount().signum() == 0) {
            loan.setStatus(LoanStatus.CLOSED);
        } else {
            loan.setStatus(LoanStatus.ACTIVE);
        }

        return toResponse(loanRepository.save(loan));
    }

    public void delete(Long id) {
        loanRepository.delete(find(id));
    }

    private Loan find(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found: " + id));
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getLoanId(),
                loan.getLoanType(),
                loan.getLoanAmount(),
                loan.getInterestRate(),
                loan.getDurationMonths(),
                loan.getAmountRepaid(),
                loan.getOutstandingAmount(),
                loan.getStatus(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getCustomer().getCustomerId()
        );
    }
}