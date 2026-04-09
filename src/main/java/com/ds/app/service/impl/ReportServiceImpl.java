package com.ds.app.service.impl;

import com.ds.app.dto.response.EmployeeInvestmentResponseDTO;
import com.ds.app.dto.response.SalaryJobResponseDTO;
import com.ds.app.dto.response.SalaryRecordResponseDTO;
import com.ds.app.dto.response.CompanyDetailDTO;
import com.ds.app.entity.*;
import com.ds.app.enums.*;
import com.ds.app.repository.*;
import com.ds.app.service.EmployeeService;
import com.ds.app.service.ReportService;

import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SalaryRecordRepository salaryRecordRepository;
    private final EmployeeInvestmentRepository investmentRepository;
    private final EmployeeBankAccountRepository bankAccountRepository;
    private final SalaryJobRepository salaryJobRepository;
    private final EmployeeRepository employeeRepository;

<<<<<<< HEAD
    // Report 1: Summary dashboard 
    // Returns key counts across the whole system
=======
    private final iEmployeeRepository employeeRepo;
    private final iEscalationRepository escalationRepo;
    private final iCompanyRepository companyRepo;
    private final iDepartmentRepository deptRepo;
    private final EmployeeService employeeService;

    // ================= FINANCE =================

    @Override
    public Page<SalaryRecordResponseDTO> getSalaryRegister(String month, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creditedAt").descending());

        if (month != null && !month.isBlank()) {
            YearMonth targetMonth = YearMonth.parse(month);
            return salaryRecordRepository.findBySalaryMonth(targetMonth, pageable)
                    .map(this::mapRecordToResponse);
        }

        return salaryRecordRepository.findAll(pageable).map(this::mapRecordToResponse);
    }

    @Override
    public Page<EmployeeInvestmentResponseDTO> getComplianceReport(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("declaredAt").descending());
        return investmentRepository
                .findByComplianceStatus(ComplianceStatus.NON_COMPLIANT, pageable)
                .map(this::mapInvestmentToResponse);
    }

    @Override
    public Map<String, Long> getBankAccountStatusReport() {
        Map<String, Long> report = new LinkedHashMap<>();
        report.put("total", bankAccountRepository.count());
        report.put("pending", bankAccountRepository.countByValidationStatus(BankValidationStatus.PENDING));
        report.put("approved", bankAccountRepository.countByValidationStatus(BankValidationStatus.APPROVED));
        report.put("rejected", bankAccountRepository.countByValidationStatus(BankValidationStatus.REJECTED));
        return report;
    }

    @Override
    public Page<SalaryJobResponseDTO> getSalaryJobReport(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return salaryJobRepository.findAll(pageable).map(this::mapJobToResponse);
    }

    // ================= HR =================

>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("totalEmployees", employeeRepo.countByIsDeletedFalse());
        summary.put("escalatedEmployees", employeeRepo.countByIsEscalatedTrue());
        summary.put("openEscalations", escalationRepo.countByStatus(EscalationStatus.OPEN));
        summary.put("inProgressEscalations", escalationRepo.countByStatus(EscalationStatus.IN_PROGRESS));
        summary.put("resolvedEscalations", escalationRepo.countByStatus(EscalationStatus.RESOLVED));
        return summary;
    }

<<<<<<< HEAD
    // Report 2: Count employees per company
    // Returns { "ICICI Bank": 12, "Citi": 8 }
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Long> countGroupByCompany() {
        List<Object[]> rows = employeeRepo.countGroupByCompany();
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Long companyId = (Long) row[0];
            Long count = (Long) row[1];
            String name = companyRepo.findById(companyId)
                    .map(Company::getName)
                    .orElse("Company-" + companyId);
            result.put(name, count);
        }
        return result;
    }

<<<<<<< HEAD
    // Report 3: Count employees per department 
    // Returns { "Risk & Compliance": 5, "Tech": 10 }
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Long> countGroupByDepartment() {
        List<Object[]> rows = employeeRepo.countGroupByDepartment();
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Long deptId = (Long) row[0];
            Long count = (Long) row[1];
            String name = deptRepo.findById(deptId)
                    .map(Department::getName)
                    .orElse("Dept-" + deptId);
            result.put(name, count);
        }
        return result;
    }

<<<<<<< HEAD
    //Report 4: Count employees per type 
    // Returns { "FRESHER": 20, "EXPERIENCED": 15, "CERTIFIED": 8 }
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Long> countGroupByEmployeeType() {
        List<Object[]> rows = employeeRepo.countGroupByEmployeeType();
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String type = row[0] != null ? row[0].toString() : "NOT_SET";
            result.put(type, (Long) row[1]);
        }
        return result;
    }

<<<<<<< HEAD
    //  Report 5: Count employees per status 
    // Returns { "ACTIVE": 40, "INACTIVE": 3, "TERMINATED": 1 }
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Long> countGroupByStatus() {
        List<Object[]> rows = employeeRepo.countGroupByStatus();
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put(row[0].toString(), (Long) row[1]);
        }
        return result;
    }

<<<<<<< HEAD
    //Report 6: Count in a specific company
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public long countByCompany(Long companyId) {
        return employeeService.countByCompany(companyId);
    }

<<<<<<< HEAD
    // Report 7: Count in a specific department 
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public long countByDepartment(Long deptId) {
        return employeeService.countByDepartment(deptId);
    }
<<<<<<< HEAD
    
    
    //Report 8: Company perspective business Report
=======

>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
    public Map<String, Map<String, List<String>>> getCompanyPerspectiveReport() {
        List<CompanyDetailDTO> data = employeeRepo.getDetailedCompanyReport();

        return data.stream().collect(Collectors.groupingBy(
                CompanyDetailDTO::getCompanyName,
                Collectors.groupingBy(
                        CompanyDetailDTO::getDepartmentName,
                        Collectors.mapping(
                                dto -> dto.getEmployeeName() + " (Project: " +
                                        (dto.getProjectName() != null ? dto.getProjectName() : "Bench") + ")",
                                Collectors.toList()
                        )
                )
        ));
    }

    // ================= MAPPERS =================

    private String maskAccount(String acc) {
        if (acc == null || acc.length() < 4) return "****";
        return "****" + acc.substring(acc.length() - 4);
    }

    private SalaryRecordResponseDTO mapRecordToResponse(SalaryRecord record) {
        SalaryRecordResponseDTO dto = new SalaryRecordResponseDTO();
        dto.setId(record.getId());
        dto.setEmployeeId(record.getEmployee().getUserId());
        dto.setEmployeeName(record.getEmployee().getFirstName() + " " + record.getEmployee().getLastName());
        dto.setSalaryMonth(record.getSalaryMonth().toString());
        dto.setGrossSalary(record.getGrossSalary());
        dto.setDeductions(record.getDeductions());
        dto.setNetSalary(record.getNetSalary());
        dto.setPaymentStatus(record.getPaymentStatus());
        dto.setCreditedAt(record.getCreditedAt());
        if (record.getBankAccount() != null) {
            dto.setBankAccountMasked(maskAccount(record.getBankAccount().getAccountNumber()));
            dto.setBankName(record.getBankAccount().getBank().getBankName());
        }
        return dto;
    }

    private EmployeeInvestmentResponseDTO mapInvestmentToResponse(EmployeeInvestment inv) {
        EmployeeInvestmentResponseDTO dto = new EmployeeInvestmentResponseDTO();
        dto.setEmpInvestmentId(inv.getEmpInvestmentId());
        dto.setEmployeeId(inv.getEmployee().getUserId());
        dto.setEmployeeName(inv.getEmployee().getFirstName() + " " + inv.getEmployee().getLastName());
        dto.setInvestmentType(inv.getInvestmentType());
        dto.setDeclaredAmount(inv.getDeclaredAmount());
        dto.setComplianceStatus(inv.getComplianceStatus());
        dto.setReviewNote(inv.getReviewNote());
        dto.setDeclaredAt(inv.getDeclaredAt());
        return dto;
    }

    private SalaryJobResponseDTO mapJobToResponse(SalaryJob job) {
        SalaryJobResponseDTO dto = new SalaryJobResponseDTO();
        dto.setId(job.getId());
        dto.setJobName(job.getJobName());
        dto.setScheduledDateTime(job.getScheduledDateTime());
        dto.setTargetMonth(job.getTargetMonth().toString());
        dto.setJobStatus(job.getJobStatus());
        dto.setTotalEmployees(job.getTotalEmployees());
        dto.setSuccessCount(job.getSuccessCount());
        dto.setFailureCount(job.getFailureCount());
        dto.setCreatedBy(job.getCreatedBy());
        return dto;
    }
}