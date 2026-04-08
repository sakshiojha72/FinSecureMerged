package com.ds.app.entity;

import com.ds.app.enums.ComplianceStatus;
import com.ds.app.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmployeeInvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empInvestmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType investmentType;

    //  Mutual Fund (only for MUTUAL_FUND)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_id")
    private FinanceInvestment mutualFund;

    //  Security name (DIRECT_EQUITY / BONDS)
    @Column(length = 150)
    private String securityName;

    private Double declaredAmount;

    //  Company being invested in (DIRECT_EQUITY / BONDS)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;   

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceStatus complianceStatus = ComplianceStatus.PENDING_REVIEW;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(length = 500)
    private String reviewNote;

    @CreationTimestamp
    @Column(name = "declared_at", nullable = false, updatable = false)
    private LocalDateTime declaredAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime upadtedAt;
}