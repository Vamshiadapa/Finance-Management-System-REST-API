package com.finance.controller;

import com.finance.dto.InvestmentRequest;

import com.finance.dto.InvestmentResponse;
import com.finance.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService service;

    @PostMapping
    public ResponseEntity<InvestmentResponse> save(
            @Valid @RequestBody InvestmentRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @GetMapping
    public Page<InvestmentResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(page, size);
    }
    @GetMapping("/{id}")
    public InvestmentResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}