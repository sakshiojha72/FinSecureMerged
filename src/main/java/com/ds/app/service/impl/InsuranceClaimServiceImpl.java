package com.ds.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.app.dto.request.ClaimRequestDTO;
import com.ds.app.dto.request.ClaimStatusUpdateDTO;
import com.ds.app.dto.response.ClaimResponseDTO;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeInsurance;
import com.ds.app.entity.InsuranceClaim;
import com.ds.app.enums.ClaimStatus;
import com.ds.app.enums.InsuranceStatus;
import com.ds.app.exception.BusinessRuleException;
import com.ds.app.exception.ConflictException;
import com.ds.app.exception.ResourceNotFoundException;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.InsuranceClaimRepository;
import com.ds.app.service.InsuranceClaimService;

@Service
public class InsuranceClaimServiceImpl implements InsuranceClaimService {

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @Override
    public ClaimResponseDTO raiseClaim(ClaimRequestDTO dto, Long employeeId) throws ResourceNotFoundException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        EmployeeInsurance insurance = employeeInsuranceRepository
                .findById(dto.getEmployeeInsuranceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insurance record for the employee not found"));

        if (insurance.getStatus() == InsuranceStatus.EXPIRED) {
            throw new BusinessRuleException(
                    "Cannot raise a claim on an expired insurance");
        }

        if (insuranceClaimRepository.existsByEmployee_UserIdAndStatus(
                employeeId, ClaimStatus.PENDING)) {
            throw new ConflictException(
                    "You already have a pending claim. Resolve it before raising a new one");
        }

        InsuranceClaim claim = new InsuranceClaim();
        claim.setEmployee(employee);
        claim.setEmployeeInsurance(insurance);
        claim.setClaimAmount(dto.getClaimAmount());
        claim.setReason(dto.getReason());
        claim.setStatus(ClaimStatus.PENDING);
        claim.setRaisedAt(LocalDateTime.now());

        InsuranceClaim saved = insuranceClaimRepository.save(claim);
        return mapToClaimResponse(saved);
    }

    @Override
    public List<ClaimResponseDTO> getEmployeeClaims(Long employeeId) {
        return insuranceClaimRepository
                .findByEmployee_UserId(employeeId)
                .stream()
                .map(this::mapToClaimResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClaimResponseDTO updateClaimStatus(ClaimStatusUpdateDTO dto) throws ResourceNotFoundException {

        InsuranceClaim claim = insuranceClaimRepository
                .findById(dto.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Claim not found with id: " + dto.getClaimId()));

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BusinessRuleException(
                    "Only PENDING claims can be approved or rejected");
        }

        if (dto.getStatus() == ClaimStatus.PENDING) {
            throw new BusinessRuleException(
                    "Cannot set claim status back to PENDING");
        }

        claim.setStatus(dto.getStatus());
        claim.setAdminRemarks(dto.getAdminRemarks());
        claim.setResolvedBy(dto.getResolvedBy());
        claim.setResolvedAt(LocalDateTime.now());

        InsuranceClaim saved = insuranceClaimRepository.save(claim);
        return mapToClaimResponse(saved);
    }

    @Override
    public List<ClaimResponseDTO> getAllClaims(ClaimStatus status) {
        if (status != null) {
            return insuranceClaimRepository.findByStatus(status)
                    .stream()
                    .map(this::mapToClaimResponse)
                    .collect(Collectors.toList());
        }
        return insuranceClaimRepository.findAll()
                .stream()
                .map(this::mapToClaimResponse)
                .collect(Collectors.toList());
    }

    private ClaimResponseDTO mapToClaimResponse(InsuranceClaim claim) {
        ClaimResponseDTO dto = new ClaimResponseDTO();
        dto.setClaimId(claim.getId());
        dto.setEmployeeId(claim.getEmployee().getUserId());

        String firstName = claim.getEmployee().getFirstName();
        String lastName  = claim.getEmployee().getLastName();
        dto.setEmployeeName(
            (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank())
                ? firstName + " " + lastName
                : claim.getEmployee().getUsername()
        );

        dto.setEmployeeInsuranceId(claim.getEmployeeInsurance().getId());
        dto.setPlanName(claim.getEmployeeInsurance().getInsurancePlan().getPlanName());
        dto.setClaimAmount(claim.getClaimAmount());
        dto.setReason(claim.getReason());
        dto.setStatus(claim.getStatus());
        dto.setRaisedAt(claim.getRaisedAt());
        dto.setResolvedAt(claim.getResolvedAt());
        dto.setResolvedBy(claim.getResolvedBy());
        dto.setAdminRemarks(claim.getAdminRemarks());
        dto.setCreatedAt(claim.getCreatedAt());
        return dto;
    }
}