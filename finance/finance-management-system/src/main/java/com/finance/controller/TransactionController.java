package com.finance.controller;
import com.finance.dto.*; 
import com.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;
    @PostMapping public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest r) { return ResponseEntity.status(201).body(service.create(r)); }
    @GetMapping public List<TransactionResponse> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public TransactionResponse get(@PathVariable Long id) { return service.getById(id); }
    @PutMapping("/{id}") public TransactionResponse update(@PathVariable Long id, @Valid @RequestBody TransactionRequest r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
