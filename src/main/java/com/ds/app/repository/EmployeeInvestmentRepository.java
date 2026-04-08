package com.ds.app.repository;

import com.ds.app.entity.EmployeeInvestment;
import com.ds.app.enums.ComplianceStatus;
import com.ds.app.enums.InvestmentType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeInvestmentRepository
        extends JpaRepository<EmployeeInvestment, Long> {

    //  Get all investments of one employee
    Page<EmployeeInvestment> findByEmployee_UserId(Long userId, Pageable pageable);

    //   ONE mutual fund per employee
    // FinanceInvestment primary key = mutualFundId
    boolean existsByEmployee_UserIdAndMutualFund_MutualFundId(
        Long userId,
        Long mutualFundId
    );

    // ✅ Enforce ONE company investment per employee (DIRECT_EQUITY / BONDS)
    // Company primary key = id
    boolean existsByEmployee_UserIdAndCompany_Id(
        Long userId,
        Long companyId
    );

    // ✅ Get all investments by compliance status
    Page<EmployeeInvestment> findByComplianceStatus(
        ComplianceStatus status,
        Pageable pageable
    );

    // ✅ Get investments by investment type
    Page<EmployeeInvestment> findByInvestmentType(
        InvestmentType type,
        Pageable pageable
    );

    // ✅ Prevent duplicate SECURITY NAME declarations
    boolean existsByEmployee_UserIdAndSecurityName(
        Long userId,
        String securityName
    );

    // ✅ Get investments by employer company (HR / Admin use case)
    Page<EmployeeInvestment> findByEmployee_Company_Id(
        Long companyId,
        Pageable pageable
    );
}