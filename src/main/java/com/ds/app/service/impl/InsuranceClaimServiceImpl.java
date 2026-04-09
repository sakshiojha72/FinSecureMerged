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
import com.ds.app.exception.ResourceNotFoundException2;
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
    public ClaimResponseDTO raiseClaim(ClaimRequestDTO dto, Long employeeId) throws ResourceNotFoundException2 {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException2("Employee not found"));

        EmployeeInsurance insurance = employeeInsuranceRepository
                .findById(dto.getEmployeeInsuranceId())
                .orElseThrow(() -> new ResourceNotFoundException2(
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
	            .map(claim -> mapToClaimResponse(claim))
	            .collect(Collectors.toList());
	}		


	@Override
	public ClaimResponseDTO updateClaimStatus(ClaimStatusUpdateDTO dto) {
	    // claim must exist
        InsuranceClaim claim = insuranceClaimRepository
                .findById(dto.getClaimId())
                .orElseThrow(() -> new RuntimeException(
                    "Claim not found with id: " + dto.getClaimId()
                ));

        // 1: only PENDING claims can be approved or rejected
        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException(
                "Only PENDING claims can be approved or rejected"
            );
        }

        // RULE 2: status can only move forward
        if (dto.getStatus() == ClaimStatus.PENDING) {
            throw new RuntimeException(
                "Cannot set claim status back to PENDING"
            );
        }

        // update claim with admin decision
        claim.setStatus(dto.getStatus());
        claim.setAdminRemarks(dto.getAdminRemarks()); // mandatory — audit trail
        claim.setResolvedBy(dto.getResolvedBy());     // which admin resolved
        claim.setResolvedAt(LocalDateTime.now());      // timestamp of resolution

        claim.setStatus(ClaimStatus.PENDING);
        claim.setRaisedAt(LocalDateTime.now());


        InsuranceClaim saved = insuranceClaimRepository.save(claim);
        return mapToClaimResponse(saved);
    }


	

    // MAPPERS



    private ClaimResponseDTO mapToClaimResponse(InsuranceClaim claim) {
        ClaimResponseDTO dto = new ClaimResponseDTO();
        dto.setClaimId(claim.getId());
        dto.setEmployeeId(claim.getEmployee().getUserId());

        dto.setEmployeeName(claim.getEmployee().getFirstName()+" "+ claim.getEmployee().getLastName());
        dto.setEmployeeInsuranceId(
            claim.getEmployeeInsurance().getId()
        );
        dto.setPlanName(
            claim.getEmployeeInsurance().getInsurancePlan().getPlanName()
        );


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

}

