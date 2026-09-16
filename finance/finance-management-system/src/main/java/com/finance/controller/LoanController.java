package com.finance.controller;
import com.finance.dto.*;
import com.finance.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService service;
    @PostMapping public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest r) { return ResponseEntity.status(201).body(service.create(r)); }
    @GetMapping public List<LoanResponse> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public LoanResponse get(@PathVariable Long id) { return service.getById(id); }
    @PutMapping("/{id}") public LoanResponse update(@PathVariable Long id, @Valid @RequestBody LoanRequest r) { return service.update(id, r); }
    @PatchMapping("/{id}/approve") public LoanResponse approve(@PathVariable Long id) { return service.approve(id); }
    @PostMapping("/{id}/repay") public LoanResponse repay(@PathVariable Long id, @Valid @RequestBody RepaymentRequest r) { return service.repay(id, r); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
