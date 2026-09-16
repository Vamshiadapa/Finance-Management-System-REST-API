package com.finance.controller;

import com.finance.dto.LoanDocumentResponse;
import com.finance.entity.enums.DocumentType;
import com.finance.service.LoanDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans/{loanId}/documents")
@RequiredArgsConstructor
public class LoanDocumentController {

    private final LoanDocumentService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LoanDocumentResponse> upload(
            @PathVariable Long loanId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") DocumentType documentType) throws IOException {

        return ResponseEntity.status(201)
                .body(service.upload(loanId, file, documentType));
    }

    @GetMapping
    public List<LoanDocumentResponse> getByLoan(@PathVariable Long loanId) {
        return service.getByLoan(loanId);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long documentId) throws MalformedURLException {

        var f = service.download(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(f.contentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + f.originalFileName() + "\""
                )
                .body(f.resource());
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long documentId) throws IOException {

        service.delete(documentId);
        return ResponseEntity.noContent().build();
    }
}