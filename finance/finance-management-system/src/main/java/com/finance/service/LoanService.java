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
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    // Create Loan
    public LoanResponse create(LoanRequest r) {

        Customer c = customerRepository.findById(r.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + r.customerId()
                        )
                );

        // Check Date of Birth
        if (c.getDateOfBirth() == null) {
            throw new InvalidTransactionException(
                    "Date of birth is required to apply for a loan"
            );
        }

        // Check customer age
        int age = Period.between(
                c.getDateOfBirth(),
                LocalDate.now()
        ).getYears();

        if (age < 18) {
            throw new InvalidTransactionException(
                    "Customer must be 18 years or older to apply for a loan"
            );
        }

        // Calculate interest
        BigDecimal interest = r.loanAmount()
                .multiply(r.interestRate())
                .multiply(BigDecimal.valueOf(r.durationMonths()))
                .divide(BigDecimal.valueOf(1200));

        // Calculate total payable
        BigDecimal totalPayable = r.loanAmount().add(interest);

        Loan loan = Loan.builder()
                .loanType(r.loanType())
                .loanAmount(r.loanAmount())
                .interestRate(r.interestRate())
                .durationMonths(r.durationMonths())
                .totalPayable(totalPayable)
                .amountRepaid(BigDecimal.ZERO)
                .outstandingAmount(totalPayable)
                .status(LoanStatus.PENDING)
                .startDate(LocalDate.now())
                .customer(c)
                .build();

        return toResponse(loanRepository.save(loan));
    }

    // Get All Loans
    public List<LoanResponse> getAll() {

        return loanRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get Loan By ID
    public LoanResponse getById(Long id) {

        return toResponse(find(id));
    }

    // Update Loan
    public LoanResponse update(Long id, LoanRequest r) {

        Loan loan = find(id);

        Customer customer = customerRepository.findById(r.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + r.customerId()
                        )
                );

        // Calculate interest
        BigDecimal interest = r.loanAmount()
                .multiply(r.interestRate())
                .multiply(BigDecimal.valueOf(r.durationMonths()))
                .divide(BigDecimal.valueOf(1200));

        // Calculate total payable
        BigDecimal totalPayable = r.loanAmount().add(interest);

        loan.setLoanType(r.loanType());
        loan.setLoanAmount(r.loanAmount());
        loan.setInterestRate(r.interestRate());
        loan.setDurationMonths(r.durationMonths());
        loan.setTotalPayable(totalPayable);
        loan.setCustomer(customer);

        loan.setOutstandingAmount(
                totalPayable
                        .subtract(loan.getAmountRepaid())
                        .max(BigDecimal.ZERO)
        );

        return toResponse(loanRepository.save(loan));
    }

    // Approve Loan
    @Transactional
    public LoanResponse approve(Long id) {

        Loan loan = find(id);

        loan.setStatus(LoanStatus.APPROVED);

        loan.setStartDate(LocalDate.now());

        loan.setEndDate(
                LocalDate.now()
                        .plusMonths(loan.getDurationMonths())
        );

        return toResponse(loanRepository.save(loan));
    }

    // Repay Loan
    @Transactional
    public LoanResponse repay(Long id, RepaymentRequest r) {

        Loan loan = find(id);

        if (loan.getStatus() != LoanStatus.APPROVED
                && loan.getStatus() != LoanStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Loan must be approved or active before repayment."
            );
        }

        if (r.amount().compareTo(loan.getOutstandingAmount()) > 0) {

            throw new IllegalArgumentException(
                    "Repayment cannot exceed outstanding amount."
            );
        }

        loan.setAmountRepaid(
                loan.getAmountRepaid()
                        .add(r.amount())
        );

        loan.setOutstandingAmount(
                loan.getOutstandingAmount()
                        .subtract(r.amount())
        );

        if (loan.getOutstandingAmount().signum() == 0) {

            loan.setStatus(LoanStatus.CLOSED);

        } else {

            loan.setStatus(LoanStatus.ACTIVE);
        }

        return toResponse(loanRepository.save(loan));
    }

    // Delete Loan
    public void delete(Long id) {

        loanRepository.delete(find(id));
    }

    // Find Loan
    private Loan find(Long id) {

        return loanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found: " + id
                        )
                );
    }

    // Convert Entity to Response
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