package com.finance.controller;

import com.finance.entity.AuditLog;
import com.finance.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping
    public List<AuditLog> getAll() {
        return service.getAll();
    }
}