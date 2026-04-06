package com.ds.app.dto.request;

import java.time.LocalDate;

import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.EmploymentType;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHRUpdateDTO {
 
    // ── Employee code ──────────────────────────────────────────────────
    // HR can reassign an employeeCode if needed.
    // Service checks for duplicates before saving.
    @Size(max = 20, message = "Employee code cannot exceed 20 characters")
    private String employeeCode;
 
    // ── Professional assignment ────────────────────────────────────────
    @Size(max = 100, message = "Department cannot exceed 100 characters")
    private String department;
 
    @Size(max = 100, message = "Designation cannot exceed 100 characters")
    private String designation;
 
    private EmploymentType employmentType;
 
    private EmployeeExperience employeeExperience;
 
    // ── Joining date ───────────────────────────────────────────────────
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;
 
    // ── Certification ──────────────────────────────────────────────────
    // Rule: if certificationStatus = CERTIFIED,
    //       certificationName and certificationExpiryDate become mandatory.
    //       The service enforces this — not the DTO.
    private CertificationStatus certificationStatus;
 
    @Size(max = 100, message = "Certification name cannot exceed 100 characters")
    private String certificationName;
 
    
}
