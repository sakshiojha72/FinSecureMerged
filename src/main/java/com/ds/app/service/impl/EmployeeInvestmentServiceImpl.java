package com.ds.app.service.impl;

import com.ds.app.dto.request.EmployeeInvestmentRequestDTO;
import com.ds.app.dto.request.InvestmentReviewRequestDTO;
import com.ds.app.dto.response.EmployeeInvestmentResponseDTO;
import com.ds.app.entity.Company;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeInvestment;
import com.ds.app.entity.FinanceInvestment;
import com.ds.app.enums.ComplianceStatus;
import com.ds.app.enums.FundStatus;
import com.ds.app.enums.InvestmentType;
import com.ds.app.exception.InvestmentComplianceException;
import com.ds.app.exception.ResourceNotFoundException;
import com.ds.app.repository.CompanyRepository;
import com.ds.app.repository.EmployeeInvestmentRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.FinanceInvestmentRepository;
import com.ds.app.repository.iAppUserRepository;
import com.ds.app.service.EmployeeInvestmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EmployeeInvestmentServiceImpl implements EmployeeInvestmentService {

    @Autowired
    private EmployeeInvestmentRepository employeeInvestmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FinanceInvestmentRepository financeInvestmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private iAppUserRepository appUserRepository;

    /* =========================================================
       DECLARE INVESTMENT 
       ========================================================= */
    @Override
    public EmployeeInvestmentResponseDTO declareInvestment(
            Long empId,
            EmployeeInvestmentRequestDTO dto
    ) throws ResourceNotFoundException, InvestmentComplianceException {

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );

        EmployeeInvestment.EmployeeInvestmentBuilder builder =
                EmployeeInvestment.builder()
                        .employee(employee)
                        .investmentType(dto.getInvestmentType())
                        .declaredAmount(dto.getDeclaredAmount());

        /* =====================================================
           MUTUAL FUND
           ===================================================== */
        if (dto.getInvestmentType() == InvestmentType.MUTUAL_FUND) {

            if (dto.getFundId() == null) {
                throw new InvestmentComplianceException(
                        "fundId is required for MUTUAL_FUND"
                );
            }

            // ✅ Uniqueness: one fund per employee
            boolean alreadyInvested =
                    employeeInvestmentRepository
                            .existsByEmployee_UserIdAndMutualFund_MutualFundId(
                                    empId,
                                    dto.getFundId()
                            );

            if (alreadyInvested) {
                throw new InvestmentComplianceException(
                        "Employee has already invested in this mutual fund"
                );
            }

            FinanceInvestment fund =
                    financeInvestmentRepository.findById(dto.getFundId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Fund not found")
                            );

            if (fund.getStatus() != FundStatus.WHITELISTED) {
                throw new InvestmentComplianceException(
                        "Fund '" + fund.getFundName()
                                + "' is BLACKLISTED. Only whitelisted funds are allowed."
                );
            }

            builder.mutualFund(fund);
        }

        /* =====================================================
           DIRECT EQUITY / BONDS
           ===================================================== */
        else if (dto.getInvestmentType() == InvestmentType.DIRECT_EQUITY
                || dto.getInvestmentType() == InvestmentType.BONDS) {

            if (dto.getCompanyId() == null) {
                throw new InvestmentComplianceException(
                        "companyId is required for DIRECT_EQUITY or BONDS"
                );
            }

            //  Uniqueness: one company/client per employee
            boolean alreadyInvested =
                    employeeInvestmentRepository
                            .existsByEmployee_UserIdAndCompany_Id(
                                    empId,
                                    dto.getCompanyId()
                            );

            if (alreadyInvested) {
                throw new InvestmentComplianceException(
                        "Employee has already invested in this company"
                );
            }

            Company company =
                    companyRepository.findById(dto.getCompanyId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Company not found")
                            );

            builder
                    .company(company)        
                    .securityName(dto.getSecurityName());

            if (Boolean.FALSE.equals(company.getRestrictsInvestment())) {
              
                builder.complianceStatus(ComplianceStatus.COMPLIANT);
            } else {
                //  Restriction exists then needs finance review
                builder.complianceStatus(ComplianceStatus.PENDING_REVIEW);
            }
        }

        EmployeeInvestment saved =
                employeeInvestmentRepository.save(builder.build());

        return mapToResponse(saved);
    }

    /* =========================================================
       REVIEW INVESTMENT
       ========================================================= */
    @Override
    public EmployeeInvestmentResponseDTO reviewInvestment(
            Long investmentId,
            InvestmentReviewRequestDTO dto,
            Long reviewedBy
    ) throws ResourceNotFoundException, InvestmentComplianceException {

        EmployeeInvestment investment =
                employeeInvestmentRepository.findById(investmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Investment not found with id: " + investmentId
                                )
                        );

        if (investment.getComplianceStatus() == ComplianceStatus.COMPLIANT) {
            throw new InvestmentComplianceException(
                    "Investment is already compliant"
            );
        }

        investment.setComplianceStatus(dto.getComplianceStatus());
        investment.setReviewNote(dto.getReviewNote());
        investment.setReviewedBy(reviewedBy);

        return mapToResponse(
                employeeInvestmentRepository.save(investment)
        );
    }

    /* =========================================================
       FETCH METHODS
       ========================================================= */
    @Override
    public EmployeeInvestmentResponseDTO getInvestmentById(Long id)
            throws ResourceNotFoundException {

        EmployeeInvestment investment =
                employeeInvestmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Investment not found with id: " + id
                                )
                        );

        return mapToResponse(investment);
    }

    @Override
    public Page<EmployeeInvestmentResponseDTO> getAllInvestments(
            Long employeeId,
            int page,
            int size
    ) throws ResourceNotFoundException {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + employeeId
                        )
                );

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("declaredAt").descending());

        return employeeInvestmentRepository
                .findByEmployee_UserId(employeeId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<EmployeeInvestmentResponseDTO> getByComplianceStatus(
            ComplianceStatus status,
            int page,
            int size
    ) {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by("declaredAt").descending());

        return employeeInvestmentRepository
                .findByComplianceStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<EmployeeInvestmentResponseDTO> getInvestmentsByEmployee(
            Long employeeId,
            int page,
            int size
    ) throws ResourceNotFoundException {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + employeeId
                        )
                );

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("declaredAt").descending());

        return employeeInvestmentRepository
                .findByEmployee_UserId(employeeId, pageable)
                .map(this::mapToResponse);
    }

    /* =========================================================
       RESPONSE MAPPER
       ========================================================= */
    private EmployeeInvestmentResponseDTO mapToResponse(EmployeeInvestment investment) {

    EmployeeInvestmentResponseDTO dto = new EmployeeInvestmentResponseDTO();

    dto.setEmpInvestmentId(investment.getEmpInvestmentId());
    dto.setEmployeeId(investment.getEmployee().getUserId());
    dto.setEmployeeName(
            investment.getEmployee().getFirstName() + " "
            + investment.getEmployee().getLastName()
    );

    dto.setInvestmentType(investment.getInvestmentType());
    dto.setDeclaredAmount(investment.getDeclaredAmount());
    dto.setComplianceStatus(investment.getComplianceStatus());
    dto.setReviewNote(investment.getReviewNote());
    dto.setDeclaredAt(investment.getDeclaredAt());
    dto.setUpdatedAt(investment.getUpadtedAt());

    /* ===========================
       COMPANY BASED INVESTMENTS
       =========================== */
    if (investment.getCompany() != null) {

        dto.setCompanyName(investment.getCompany().getName());
        dto.setIsRestrictedSecurity(
                Boolean.TRUE.equals(
                        investment.getCompany().getRestrictsInvestment()
                )
        );
    } else {
        // Mutual funds are never restricted at company level
        dto.setIsRestrictedSecurity(false);
    }

    /* ===========================
       MUTUAL FUND INVESTMENTS
       =========================== */
    if (investment.getMutualFund() != null) {
        dto.setFundName(investment.getMutualFund().getFundName());
        dto.setFundCode(investment.getMutualFund().getFundCode());
    }

    if (investment.getSecurityName() != null) {
        dto.setSecurityName(investment.getSecurityName());
    }

    if (investment.getReviewedBy() != null) {
        appUserRepository.findById(investment.getReviewedBy())
                .ifPresent(user ->
                        dto.setReviewedBy(user.getUsername())
                );
    }

    return dto;
}
}