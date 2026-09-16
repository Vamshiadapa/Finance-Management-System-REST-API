package com.finance.controller;

import com.finance.dto.LoanRequest;
import com.finance.dto.LoanResponse;
import com.finance.dto.RepaymentRequest;
import com.finance.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping
    public ResponseEntity<LoanResponse> save(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @GetMapping
    public Page<LoanResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(page, size);
    }

    @GetMapping("/{id}")
    public LoanResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/approve")
    public LoanResponse approve(@PathVariable Long id) {
        return service.approve(id);
    }

    @PostMapping("/{id}/repay")
    public LoanResponse repay(
            @PathVariable Long id,
            @Valid @RequestBody RepaymentRequest request) {
        return service.repay(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}