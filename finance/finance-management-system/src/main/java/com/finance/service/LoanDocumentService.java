package com.finance.service;

import com.finance.dto.LoanDocumentResponse;
import com.finance.entity.Loan;
import com.finance.entity.LoanDocument;
import com.finance.entity.enums.DocumentType;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.LoanDocumentRepository;
import com.finance.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanDocumentService {
    private final LoanDocumentRepository documentRepository;
    private final LoanRepository loanRepository;

    @Value("${app.upload.dir:uploads/loan-documents}")
    private String uploadDir;

    public LoanDocumentResponse upload(Long loanId, MultipartFile file, DocumentType type) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is required.");
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + loanId));

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        String storedName = UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        Path target = dir.resolve(storedName).normalize();
        if (!target.startsWith(dir)) throw new IllegalArgumentException("Invalid file name.");

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        LoanDocument doc = LoanDocument.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedName)
                .filePath(target.toString())
                .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                .fileSize(file.getSize()).documentType(type).loan(loan).build();
        return toResponse(documentRepository.save(doc));
    }

    public List<LoanDocumentResponse> getByLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) throw new ResourceNotFoundException("Loan not found: " + loanId);
        return documentRepository.findAll().stream()
                .filter(d -> d.getLoan().getLoanId().equals(loanId))
                .map(this::toResponse).toList();
    }

    public DownloadedFile download(Long documentId) throws MalformedURLException {
        LoanDocument d = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));
        try {
            Path path = Paths.get(d.getFilePath()).toAbsolutePath().normalize();
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) throw new ResourceNotFoundException("File not found on disk.");
            return new DownloadedFile(resource, d.getContentType(), d.getOriginalFileName());
        } catch (MalformedURLException e) { throw e; }
    }

    public void delete(Long documentId) throws IOException {
        LoanDocument d = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));
        Files.deleteIfExists(Paths.get(d.getFilePath()));
        documentRepository.delete(d);
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) return "document";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
    private LoanDocumentResponse toResponse(LoanDocument d) {
        return new LoanDocumentResponse(d.getDocumentId(), d.getOriginalFileName(), d.getContentType(),
                d.getFileSize(), d.getDocumentType(), d.getUploadedAt(), d.getLoan().getLoanId());
    }
    public record DownloadedFile(Resource resource, String contentType, String originalFileName) {}
}
