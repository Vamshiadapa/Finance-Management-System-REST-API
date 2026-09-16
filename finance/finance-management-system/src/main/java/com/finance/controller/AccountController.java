package com.finance.controller;
import com.finance.dto.*;
import com.finance.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    @PostMapping public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest r) { return ResponseEntity.status(201).body(service.create(r)); }
    @GetMapping public List<AccountResponse> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public AccountResponse get(@PathVariable Long id) { return service.getById(id); }
    @PutMapping("/{id}") public AccountResponse update(@PathVariable Long id, @Valid @RequestBody AccountRequest r) { return service.update(id, r); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
