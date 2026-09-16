package com.finance.service;

import com.finance.dto.InvestmentRequest;
import com.finance.dto.InvestmentResponse;
import com.finance.entity.Customer;
import com.finance.entity.Investment;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.CustomerRepository;
import com.finance.repository.InvestmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;
    private final CustomerRepository customerRepository;

    // CREATE
    public InvestmentResponse create(InvestmentRequest r) {

        Customer c = customerRepository.findById(r.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + r.customerId()));

        Investment i = Investment.builder()
                .investmentType(r.investmentType())
                .amount(r.amount())
                .currentValue(
                        r.currentValue() == null
                                ? r.amount()
                                : r.currentValue()
                )
                .investedDate(r.investedDate())
                .maturityDate(r.maturityDate())
                .status(
                        r.status() == null
                                ? "ACTIVE"
                                : r.status()
                )
                .performanceInfo(r.performanceInfo())
                .customer(c)
                .build();

        return toResponse(repository.save(i));
    }

    // READ ALL
    public List<InvestmentResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    public InvestmentResponse getById(Long id) {
        return toResponse(find(id));
    }

    // UPDATE
    public InvestmentResponse update(Long id, InvestmentRequest r) {

        Investment i = find(id);

        Customer c = customerRepository.findById(r.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + r.customerId()));

        i.setInvestmentType(r.investmentType());
        i.setAmount(r.amount());

        i.setCurrentValue(
                r.currentValue() == null
                        ? r.amount()
                        : r.currentValue()
        );

        i.setInvestedDate(r.investedDate());
        i.setMaturityDate(r.maturityDate());

        if (r.status() != null) {
            i.setStatus(r.status());
        }

        i.setPerformanceInfo(r.performanceInfo());
        i.setCustomer(c);

        return toResponse(repository.save(i));
    }

    // DELETE
    public void delete(Long id) {
        repository.delete(find(id));
    }

    // FIND
    private Investment find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investment not found: " + id));
    }

    // RESPONSE
    private InvestmentResponse toResponse(Investment i) {

        return new InvestmentResponse(
                i.getInvestmentId(),
                i.getInvestmentType(),
                i.getAmount(),
                i.getMaturityDate(),
                i.getCurrentValue(),
                i.getPerformanceInfo(),
                i.getCustomer().getCustomerId()
        );
    }
}