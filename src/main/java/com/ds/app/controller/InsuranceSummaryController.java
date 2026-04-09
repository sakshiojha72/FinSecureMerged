package com.ds.app.controller;

import com.ds.app.dto.response.InsuranceSummaryDTO;
import com.ds.app.entity.MyUserDetails;
import com.ds.app.service.InsuranceSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finsecure/insurance/summary")
public class InsuranceSummaryController {

    @Autowired
    private InsuranceSummaryService insuranceSummaryService;

    // only EMPLOYEE can view their own summary
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/my")
    public ResponseEntity<InsuranceSummaryDTO> getMySummary() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Long employeeId = userDetails.getUser().getUserId();
        InsuranceSummaryDTO summary = insuranceSummaryService.getInsuranceSummary(employeeId);
        return ResponseEntity.ok(summary);
    }

    // ADMIN and HR can view any employee's summary
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HR')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<InsuranceSummaryDTO> getEmployeeSummary(
            @PathVariable Long employeeId) {
        InsuranceSummaryDTO summary = insuranceSummaryService.getInsuranceSummary(employeeId);
        return ResponseEntity.ok(summary);
    }
}