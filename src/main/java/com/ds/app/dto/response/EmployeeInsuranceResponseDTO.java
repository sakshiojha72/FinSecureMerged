package com.ds.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ds.app.enums.InsuranceStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInsuranceResponseDTO {

    private Long employeInsuranceId;
    private Long employeeId;
    private String employeeName;
    private String planName;
    private Double coverageAmount;
    private LocalDate assignedDate;
    private LocalDate expiryDate;
    private InsuranceStatus status;
    private LocalDateTime createdAt;
}