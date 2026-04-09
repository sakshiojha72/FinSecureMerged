package com.ds.app.controller;

import com.ds.app.dto.response.*;
import com.ds.app.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/finsecure")
public class ReportController {

    private final ReportService reportService;

    // ================= FINANCE =================

    @GetMapping("/finance/reports/salary")
    @PreAuthorize("hasAuthority('FINANCE')")
    public Page<SalaryRecordResponseDTO> getSalaryRegister(
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reportService.getSalaryRegister(month, page, size);
    }

    @GetMapping("/finance/reports/compliance")
    @PreAuthorize("hasAuthority('FINANCE')")
    public Page<EmployeeInvestmentResponseDTO> getComplianceReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reportService.getComplianceReport(page, size);
    }

    @GetMapping("/finance/reports/bank-summary")
    @PreAuthorize("hasAuthority('FINANCE')")
    public Map<String, Long> getBankAccountStatusReport() {
        return reportService.getBankAccountStatusReport();
    }

    @GetMapping("/finance/reports/salary-jobs")
    @PreAuthorize("hasAuthority('FINANCE')")
    public Page<SalaryJobResponseDTO> getSalaryJobReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reportService.getSalaryJobReport(page, size);
    }

    // ================= HR =================

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/summary")
    public ResponseEntity<Map<String, Long>> getSummary() {
        return ResponseEntity.ok(reportService.getSummary());
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/group/company")
    public ResponseEntity<Map<String, Long>> groupByCompany() {
        return ResponseEntity.ok(reportService.countGroupByCompany());
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/group/department")
    public ResponseEntity<Map<String, Long>> groupByDepartment() {
        return ResponseEntity.ok(reportService.countGroupByDepartment());
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/group/type")
    public ResponseEntity<Map<String, Long>> groupByType() {
        return ResponseEntity.ok(reportService.countGroupByEmployeeType());
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/group/status")
    public ResponseEntity<Map<String, Long>> groupByStatus() {
        return ResponseEntity.ok(reportService.countGroupByStatus());
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/count/company/{companyId}")
    public ResponseEntity<Long> countByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(reportService.countByCompany(companyId));
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/count/department/{deptId}")
    public ResponseEntity<Long> countByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(reportService.countByDepartment(deptId));
    }

    @PreAuthorize("hasAuthority('HR') or hasAuthority('ADMIN')")
    @GetMapping("/hr/reports/business")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getCompanyPerspectiveReport() {
        return ResponseEntity.ok(reportService.getCompanyPerspectiveReport());
    }
}