package com.finance.controller;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.service.TransactionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;


    
    @PostMapping
    public ResponseEntity<TransactionResponse> save(
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse response =
                service.save(request);

        return ResponseEntity.ok(response);
    }


    
    @GetMapping
    public Page<TransactionResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(page, size);
    }


    @GetMapping("/{id}")
    public TransactionResponse getById(
            @PathVariable Long id) {

        return service.getById(id);
    }


    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}