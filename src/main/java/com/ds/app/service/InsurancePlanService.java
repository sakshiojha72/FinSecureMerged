package com.ds.app.service;

import java.util.List;

import com.ds.app.dto.request.AssignInsuranceRequestDTO;
import com.ds.app.dto.request.BuyTopUpRequestDTO;
import com.ds.app.dto.request.ClaimRequestDTO;
import com.ds.app.dto.request.CreateInsurancePlanRequestDTO;
import com.ds.app.dto.request.CreateTopUpPlanRequestDTO;
import com.ds.app.dto.response.ClaimResponseDTO;
import com.ds.app.dto.response.EmployeeInsuranceResponseDTO;
import com.ds.app.dto.response.EmployeeTopUpResponseDTO;
import com.ds.app.dto.response.InsurancePlanResponseDTO;
import com.ds.app.dto.response.TopUpPlanResponseDTO;
import com.ds.app.enums.ClaimStatus;

import com.ds.app.exception.ResourceNotFoundException;
import com.ds.app.exception.ResourceNotFoundException2;


public interface InsurancePlanService {
		
	//admin creates new base plan 
	InsurancePlanResponseDTO createInsurancePlan(CreateInsurancePlanRequestDTO dto, String createdBy);

	//get all active plans (pagination)
	List<InsurancePlanResponseDTO> getAllInsurancePlans();


	void deactivateInsurancePlan(Long planId) throws ResourceNotFoundException2;
	
	//admin assign plan to employee
	EmployeeInsuranceResponseDTO assignInsurance(AssignInsuranceRequestDTO dto) throws ResourceNotFoundException2;
	
	EmployeeInsuranceResponseDTO getEmployeeInsurance(Long employeeId) throws ResourceNotFoundException2;

	
	
	
}
