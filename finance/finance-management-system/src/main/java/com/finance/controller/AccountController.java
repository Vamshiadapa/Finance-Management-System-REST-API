package com.finance.controller;

import com.finance.dto.AccountRequest;
import com.finance.dto.AccountResponse;
import com.finance.service.AccountService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Page;
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;


    
    @PostMapping
    public ResponseEntity<AccountResponse> save(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse response = service.save(request);

        return ResponseEntity.ok(response);
    }


  
    @GetMapping
    public Page<AccountResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(page, size);
    }


   
    @GetMapping("/{id}")
    public AccountResponse getById(
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