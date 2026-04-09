package com.ds.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.ds.app.dto.response.EmployeeInvestmentResponseDTO;
import com.ds.app.dto.response.SalaryJobResponseDTO;
import com.ds.app.dto.response.SalaryRecordResponseDTO;

public interface ReportService {

    // FINANCE REPORTS
    Page<SalaryRecordResponseDTO> getSalaryRegister(String month, int page, int size);

    Page<EmployeeInvestmentResponseDTO> getComplianceReport(int page, int size);

    Map<String, Long> getBankAccountStatusReport();

    Page<SalaryJobResponseDTO> getSalaryJobReport(int page, int size);

    // HR REPORTS
    Map<String, Long> getSummary();

    Map<String, Long> countGroupByCompany();

    Map<String, Long> countGroupByDepartment();

    Map<String, Long> countGroupByEmployeeType();

    Map<String, Long> countGroupByStatus();

    long countByCompany(Long companyId);

    long countByDepartment(Long deptId);

    Map<String, Map<String, List<String>>> getCompanyPerspectiveReport();
}