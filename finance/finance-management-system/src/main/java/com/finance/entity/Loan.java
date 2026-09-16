package com.finance.entity;

import com.finance.entity.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Column(nullable = false, length = 50)
    private String loanType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPayable;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal amountRepaid = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal outstandingAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private LoanStatus status = LoanStatus.PENDING;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(
        mappedBy = "loan",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<LoanDocument> documents = new ArrayList<>();
}