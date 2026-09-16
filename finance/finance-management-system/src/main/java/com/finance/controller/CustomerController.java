package com.finance.controller;
import com.finance.dto.*;
import com.finance.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;
    @PostMapping public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest r) { return ResponseEntity.status(201).body(service.create(r)); }
    @GetMapping public List<CustomerResponse> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public CustomerResponse get(@PathVariable Long id) { return service.getById(id); }
    @PutMapping("/{id}") public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody CustomerRequest r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
