package com.ds.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.app.dto.response.EmployeeTopUpResponseDTO;
import com.ds.app.dto.response.InsuranceSummaryDTO;

import com.ds.app.dto.response.TopUpPlanResponseDTO;

import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeInsurance;
import com.ds.app.entity.EmployeeTopUp;
import com.ds.app.enums.InsuranceStatus;

import com.ds.app.exception.ResourceNotFoundException;
import com.ds.app.exception.ResourceNotFoundException2;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.EmployeeTopUpRepository;
import com.ds.app.service.InsuranceSummaryService;


import lombok.RequiredArgsConstructor;


@Service
public class InsuranceSummaryServiceImpl implements InsuranceSummaryService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @Autowired
    private EmployeeTopUpRepository employeeTopUpRepository;

    @Override
    public InsuranceSummaryDTO getInsuranceSummary(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Employee not found with id: " + employeeId));

        EmployeeInsurance insurance = employeeInsuranceRepository
                .findByEmployee_UserIdAndStatus(employeeId, InsuranceStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "No active insurance found for this employee"));

        List<EmployeeTopUp> activeTopUps = employeeTopUpRepository
                .findByEmployee_UserIdAndStatus(employeeId, InsuranceStatus.ACTIVE);

        List<EmployeeTopUpResponseDTO> topUpDTOs = activeTopUps.stream()
                .map(topUp -> mapToTopUpResponse(topUp, employee))
                .collect(Collectors.toList());

        Double totalCoverage =
            insurance.getInsurancePlan().getCoverageAmount()
            + activeTopUps.stream()
                .mapToDouble(t -> t.getTopUpPlan().getAdditionalCoverage())
                .sum();

        // full summary response
        InsuranceSummaryDTO summary = new InsuranceSummaryDTO();
        summary.setEmployeeId(employee.getUserId());
        summary.setEmployeeName(employee.getFirstName()+" "+employee.getLastName());
        summary.setEmployeeInsuranceId(insurance.getId());
        summary.setBasePlanName(
            insurance.getInsurancePlan().getPlanName()
        );
        summary.setBaseCoverageAmount(
            insurance.getInsurancePlan().getCoverageAmount()
        );
        String firstName = employee.getFirstName();
        String lastName  = employee.getLastName();
        String fullName = (firstName != null && !firstName.isBlank()
                           && lastName != null && !lastName.isBlank())
                ? firstName + " " + lastName
                : employee.getUsername();

        summary.setExpiryDate(insurance.getExpiryDate());
        summary.setInsuranceStatus(insurance.getStatus());
        summary.setActiveTopUps(topUpDTOs);
        summary.setTotalCoverageAmount(totalCoverage);

       return summary;

    }
    

    private EmployeeTopUpResponseDTO mapToTopUpResponse(
            EmployeeTopUp topUp, Employee employee) {

        EmployeeTopUpResponseDTO dto = new EmployeeTopUpResponseDTO();
        dto.setEmployeeTopUpId(topUp.getId());
        dto.setEmployeeId(employee.getUserId());

        String firstName = employee.getFirstName();
        String lastName  = employee.getLastName();
        dto.setEmployeeName(
            (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank())
                ? firstName + " " + lastName
                : employee.getUsername()
        );

        dto.setTopUpName(topUp.getTopUpPlan().getTopUpName());
        dto.setAdditionalCoverage(topUp.getTopUpPlan().getAdditionalCoverage());
        dto.setPrice(topUp.getTopUpPlan().getPrice());
        dto.setPurchasedDate(topUp.getPurchasedDate());
        dto.setExpiryDate(topUp.getExpiryDate());
        dto.setStatus(topUp.getStatus());
        dto.setCreatedAt(topUp.getCreatedAt());

        return dto;
    }
     
}
