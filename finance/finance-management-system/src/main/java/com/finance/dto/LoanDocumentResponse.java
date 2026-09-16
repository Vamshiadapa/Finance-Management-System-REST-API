package com.finance.dto;
import com.finance.entity.enums.DocumentType;
import java.time.LocalDateTime;
public record LoanDocumentResponse(Long documentId, String originalFileName, String contentType,
                                    Long fileSize, DocumentType documentType, LocalDateTime uploadedAt, Long loanId) {}
