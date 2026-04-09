package com.ds.app.service.impl;

import com.ds.app.dto.request.EmployeeInvestmentRequestDTO;
import com.ds.app.dto.request.InvestmentReviewRequestDTO;
import com.ds.app.dto.response.EmployeeInvestmentResponseDTO;
import com.ds.app.entity.*;
import com.ds.app.enums.*;
import com.ds.app.exception.*;
import com.ds.app.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeInvestmentServiceImplTest {

    // ─────────────── Mocks ───────────────
    @Mock private EmployeeInvestmentRepository employeeInvestmentRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private FinanceInvestmentRepository companyMutualFundRepository;
    @Mock private CompanyRepository companyRepository;

    @InjectMocks
    private EmployeeInvestmentServiceImpl investmentService;

    private Employee employee;
    private FinanceInvestment whitelistedFund;
    private Company unrestrictedCompany;
    private Company restrictedCompany;

    @BeforeEach
    void setUp() {

        employee = new Employee();
        employee.setUserId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("Verma");

        whitelistedFund = new FinanceInvestment();
        whitelistedFund.setMutualFundId(1L);
        whitelistedFund.setFundName("ICICI Bluechip");
        whitelistedFund.setStatus(FundStatus.WHITELISTED);

        unrestrictedCompany = new Company();
        unrestrictedCompany.setId(10L);
        unrestrictedCompany.setName("Infosys");
        unrestrictedCompany.setRestrictsInvestment(false);

        restrictedCompany = new Company();
        restrictedCompany.setId(11L);
        restrictedCompany.setName("TCS");
        restrictedCompany.setRestrictsInvestment(true);
    }

    // ───────────────── MUTUAL FUND ─────────────────

    @Test
    void declareInvestment_mutualFund_whitelisted_isCompliant() throws ResourceNotFoundException, InvestmentComplianceException {

        EmployeeInvestmentRequestDTO dto = new EmployeeInvestmentRequestDTO();
        dto.setInvestmentType(InvestmentType.MUTUAL_FUND);
        dto.setFundId(1L);
        dto.setDeclaredAmount(50000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(companyMutualFundRepository.findById(1L))
                .thenReturn(Optional.of(whitelistedFund));

        when(employeeInvestmentRepository
                .existsByEmployee_UserIdAndMutualFund_MutualFundId(1L, 1L))
                .thenReturn(false);

        when(employeeInvestmentRepository.save(any()))
                .thenAnswer(invocation -> {
                    EmployeeInvestment inv = invocation.getArgument(0);
                    inv.setEmpInvestmentId(100L);
                    return inv;
                });

        EmployeeInvestmentResponseDTO response =
                investmentService.declareInvestment(1L, dto);

        assertThat(response.getComplianceStatus())
                .isEqualTo(ComplianceStatus.PENDING_REVIEW);
    }

    // ───────────────── DIRECT EQUITY ─────────────────

    @Test
    void declareInvestment_directEquity_unrestrictedCompany_isCompliant() throws ResourceNotFoundException, InvestmentComplianceException {

        EmployeeInvestmentRequestDTO dto = new EmployeeInvestmentRequestDTO();
        dto.setInvestmentType(InvestmentType.DIRECT_EQUITY);
        dto.setCompanyId(10L);
        dto.setSecurityName("Infosys Ltd");
        dto.setDeclaredAmount(75000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(companyRepository.findById(10L))
                .thenReturn(Optional.of(unrestrictedCompany));

        when(employeeInvestmentRepository
                .existsByEmployee_UserIdAndCompany_Id(1L, 10L))
                .thenReturn(false);

        when(employeeInvestmentRepository.save(any()))
                .thenAnswer(invocation -> {
                    EmployeeInvestment inv = invocation.getArgument(0);
                    inv.setEmpInvestmentId(101L);
                    return inv;
                });

        EmployeeInvestmentResponseDTO response =
                investmentService.declareInvestment(1L, dto);

        assertThat(response.getComplianceStatus())
                .isEqualTo(ComplianceStatus.COMPLIANT);
    }

    @Test
    void declareInvestment_directEquity_restrictedCompany_isPendingReview() throws ResourceNotFoundException, InvestmentComplianceException {

        EmployeeInvestmentRequestDTO dto = new EmployeeInvestmentRequestDTO();
        dto.setInvestmentType(InvestmentType.DIRECT_EQUITY);
        dto.setCompanyId(11L);
        dto.setSecurityName("TCS");
        dto.setDeclaredAmount(90000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(companyRepository.findById(11L))
                .thenReturn(Optional.of(restrictedCompany));

        when(employeeInvestmentRepository
                .existsByEmployee_UserIdAndCompany_Id(1L, 11L))
                .thenReturn(false);

        when(employeeInvestmentRepository.save(any()))
                .thenAnswer(invocation -> {
                    EmployeeInvestment inv = invocation.getArgument(0);
                    inv.setEmpInvestmentId(102L);
                    return inv;
                });

        EmployeeInvestmentResponseDTO response =
                investmentService.declareInvestment(1L, dto);

        assertThat(response.getComplianceStatus())
                .isEqualTo(ComplianceStatus.PENDING_REVIEW);
    }

    // ───────────────── VALIDATION ─────────────────

    @Test
    void declareInvestment_directEquity_withoutSecurityName_throwsException() {

        EmployeeInvestmentRequestDTO dto = new EmployeeInvestmentRequestDTO();
        dto.setInvestmentType(InvestmentType.DIRECT_EQUITY);
        dto.setCompanyId(10L);
        dto.setDeclaredAmount(20000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        assertThatThrownBy(() ->
                investmentService.declareInvestment(1L, dto))
                .isInstanceOf(InvestmentComplianceException.class)
                .hasMessageContaining("Security name");
    }

    @Test
    void declareInvestment_duplicateMutualFund_throwsException() {

        EmployeeInvestmentRequestDTO dto = new EmployeeInvestmentRequestDTO();
        dto.setInvestmentType(InvestmentType.MUTUAL_FUND);
        dto.setFundId(1L);
        dto.setDeclaredAmount(50000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeInvestmentRepository
                .existsByEmployee_UserIdAndMutualFund_MutualFundId(1L, 1L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                investmentService.declareInvestment(1L, dto))
                .isInstanceOf(InvestmentComplianceException.class)
                .hasMessageContaining("already invested");
    }

    // ───────────────── REVIEW ─────────────────

    @Test
    void reviewInvestment_alreadyCompliant_throwsException() {

        EmployeeInvestment investment = new EmployeeInvestment();
        investment.setEmpInvestmentId(1L);
        investment.setComplianceStatus(ComplianceStatus.COMPLIANT);

        when(employeeInvestmentRepository.findById(1L))
                .thenReturn(Optional.of(investment));

        InvestmentReviewRequestDTO dto =
                new InvestmentReviewRequestDTO(
                        ComplianceStatus.NON_COMPLIANT,
                        "Reject"
                );

        assertThatThrownBy(() ->
                investmentService.reviewInvestment(1L, dto, 5L))
                .isInstanceOf(InvestmentComplianceException.class)
                .hasMessageContaining("already compliant");
    }
}