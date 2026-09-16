package com.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(nullable = false, length = 100)
    private String entityName;

    @Column(length = 100)
    private String entityId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 500)
    private String details;
}