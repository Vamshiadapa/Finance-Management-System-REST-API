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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository repository;
    private final CustomerRepository customerRepository;

    public InvestmentResponse save(InvestmentRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + request.customerId()));

        Investment investment;

        if (request.investmentId() == null) {
            investment = new Investment();
        } else {
            investment = repository.findById(request.investmentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Investment not found: " + request.investmentId()));
        }

        investment.setInvestmentType(request.investmentType());
        investment.setAmount(request.amount());
        investment.setCurrentValue(
                request.currentValue() == null
                        ? request.amount()
                        : request.currentValue()
        );
        investment.setInvestedDate(request.investedDate());
        investment.setMaturityDate(request.maturityDate());

        if (request.status() != null) {
            investment.setStatus(request.status());
        } else if (investment.getStatus() == null) {
            investment.setStatus("ACTIVE");
        }

        investment.setPerformanceInfo(request.performanceInfo());
        investment.setCustomer(customer);

        return toResponse(repository.save(investment));
    }

    public Page<InvestmentResponse> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }
    public InvestmentResponse getById(Long id) {
        return toResponse(find(id));
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    private Investment find(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Investment not found: " + id));
    }

    private InvestmentResponse toResponse(Investment investment) {
        return new InvestmentResponse(
                investment.getInvestmentId(),
                investment.getInvestmentType(),
                investment.getAmount(),
                investment.getMaturityDate(),
                investment.getCurrentValue(),
                investment.getPerformanceInfo(),
                investment.getCustomer().getCustomerId()
        );
    }
}