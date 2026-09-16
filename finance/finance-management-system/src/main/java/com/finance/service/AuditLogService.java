package com.finance.service;

import com.finance.entity.AuditLog;
import com.finance.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repository;

    public void save(String action, String entityName, String entityId, String details) {

        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();

        repository.save(auditLog);
    }

    public List<AuditLog> getAll() {
        return repository.findAll();
    }
}