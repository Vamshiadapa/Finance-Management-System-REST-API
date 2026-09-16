package com.finance.repository;
import com.finance.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

}
