package com.ds.app.dto;

import java.time.LocalDate;

import com.ds.app.entity.CertificationStatus;
import com.ds.app.entity.EmployeeExperience;
import com.ds.app.entity.EmployeeSkill;
import com.ds.app.entity.EmployeeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {
	
	@NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    private Long departmentId;
    private Long companyId;
    private Long projectId;

    @NotNull(message = "Employee type is required")
    private EmployeeExperience employeeExperience;

    @NotNull(message = "Employee skill is required")
    private EmployeeSkill employeeSkill;

    @NotNull(message = "Certification status is required")
    private CertificationStatus certificationStatus;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be zero or positive")
    private Double salary;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Status is required")
    private EmployeeStatus status;

    private String profilePhotoUrl;


}//end class
