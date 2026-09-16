package com.finance.service;

import com.finance.dto.LoanDocumentResponse;
import com.finance.entity.Loan;
import com.finance.entity.LoanDocument;
import com.finance.entity.enums.DocumentType;
import com.finance.repository.LoanDocumentRepository;
import com.finance.repository.LoanRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanDocumentServiceTest {

    @Mock
    private LoanDocumentRepository documentRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanDocumentService documentService;

    @Test
    void uploadDocumentSuccessfully() throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("loan-document-test");

        ReflectionTestUtils.setField(
                documentService,
                "uploadDir",
                tempDirectory.toString()
        );

        Loan loan = new Loan();
        loan.setLoanId(1L);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "id-proof.pdf",
                "application/pdf",
                "test document".getBytes()
        );

        when(documentRepository.save(any(LoanDocument.class)))
                .thenAnswer(invocation -> {
                    LoanDocument document =
                            invocation.getArgument(0);

                    document.setDocumentId(10L);
                    return document;
                });

        LoanDocumentResponse result =
                documentService.upload(
                        1L,
                        file,
                        DocumentType.ID_PROOF
                );

        assertNotNull(result);
        assertEquals(10L, result.documentId());
        assertEquals("id-proof.pdf", result.originalFileName());
        assertEquals("application/pdf", result.contentType());
        assertEquals(DocumentType.ID_PROOF, result.documentType());
        assertEquals(1L, result.loanId());

        assertEquals(
                1,
                Files.list(tempDirectory).count()
        );

        verify(loanRepository).findById(1L);
        verify(documentRepository).save(any(LoanDocument.class));

        Files.walk(tempDirectory)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception ignored) {
                    }
                });
    }
}