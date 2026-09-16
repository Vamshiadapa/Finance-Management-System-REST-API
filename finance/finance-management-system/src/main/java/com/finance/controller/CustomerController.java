package com.finance.controller;

import com.finance.dto.CustomerRequest;
import com.finance.dto.CustomerResponse;
import com.finance.service.CustomerService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Page;
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;


   
    @PostMapping
    public ResponseEntity<CustomerResponse> save(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response = service.save(request);

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public Page<CustomerResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(page, size);
    }


    
    @GetMapping("/{id}")
    public CustomerResponse getById(
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