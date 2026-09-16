package com.finance.controller;

import com.finance.dto.*;
import com.finance.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService service;

    @PostMapping
    public ResponseEntity<InvestmentResponse> create(
            @Valid @RequestBody InvestmentRequest r) {

        return ResponseEntity
                .status(201)
                .body(service.create(r));
    }

    @GetMapping
    public List<InvestmentResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public InvestmentResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public InvestmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentRequest r) {

        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}