package com.finance.repository;
import com.finance.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
