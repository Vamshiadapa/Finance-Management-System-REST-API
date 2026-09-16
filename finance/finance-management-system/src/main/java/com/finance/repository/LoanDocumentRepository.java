package com.finance.repository;
import com.finance.entity.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoanDocumentRepository extends JpaRepository<LoanDocument, Long> {

}
