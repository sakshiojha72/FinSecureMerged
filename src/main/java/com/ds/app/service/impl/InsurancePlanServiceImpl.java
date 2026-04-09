<<<<<<< HEAD


=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
package com.ds.app.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.app.dto.request.AssignInsuranceRequestDTO;
import com.ds.app.dto.request.CreateInsurancePlanRequestDTO;
import com.ds.app.dto.response.EmployeeInsuranceResponseDTO;
import com.ds.app.dto.response.InsurancePlanResponseDTO;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeInsurance;
import com.ds.app.entity.InsurancePlan;
import com.ds.app.enums.InsuranceStatus;
<<<<<<< HEAD
=======
import com.ds.app.exception.BusinessRuleException;
import com.ds.app.exception.ConflictException;
import com.ds.app.exception.ResourceNotFoundException;
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.InsurancePlanRepository;
import com.ds.app.service.InsurancePlanService;

@Service
public class InsurancePlanServiceImpl implements InsurancePlanService {

<<<<<<< HEAD
	@Autowired
	private InsurancePlanRepository insurancePlanRepository;
	
	@Autowired
	private EmployeeInsuranceRepository employeeInsuranceRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	//rule: no two plans with same name
	@Override
	public InsurancePlanResponseDTO createInsurancePlan(CreateInsurancePlanRequestDTO dto, String createdBy) {

		if(insurancePlanRepository.existsByPlanName(dto.getPlanName()))
		{
			throw new RuntimeException(
					"Insurance plan with this name already exists");
		}
		
		//entity from req DTO 
		InsurancePlan plan = new InsurancePlan();
		plan.setPlanName(dto.getPlanName());
        plan.setCoverageAmount(dto.getCoverageAmount());
        plan.setDescription(dto.getDescription());
        plan.setCreatedBy(createdBy); //from JWT 
        plan.setIsActive(true); // always active on creation

        InsurancePlan saved = insurancePlanRepository.save(plan);
        return mapToPlanResponse(saved);	
	}

	@Override
	public List<InsurancePlanResponseDTO> getAllInsurancePlans() {

		//only return active plans
		return insurancePlanRepository.findByIsActiveTrue()
				.stream()
				.map(plan->mapToPlanResponse(plan)) //converts each plan entity to dto 
				.collect(Collectors.toList());
				}

	@Override
	public void deactivateInsurancePlan(Long planId) {

		InsurancePlan plan = insurancePlanRepository.findById(planId)
				.orElseThrow(()-> new RuntimeException(
						"Insurance plan not founf with id: "+planId));
		
		//softdelete-set isActive to false
		plan.setIsActive(false);
		insurancePlanRepository.save(plan);
	}

	@Override
	public EmployeeInsuranceResponseDTO assignInsurance(AssignInsuranceRequestDTO dto) {

		//1. employee must exist in system 
		Employee employee= employeeRepository.findById(dto.getEmployeeId())
				.orElseThrow(()->new RuntimeException(
						"Employee not found with id:" +dto.getEmployeeId()));
		
		//2. plan must exist
		InsurancePlan plan = insurancePlanRepository.findById(dto.getPlanId())
				.orElseThrow(()->new RuntimeException(
						"Insurance plan not found with id: "+dto.getPlanId()));
		
		//3. cant assign a deactivated plan
		if(!plan.getIsActive())
		{
			throw new RuntimeException(
					"Cannot assign a decativated insurance plan");
		}
		//4. employee can have only one actice insurance at a time
		if(employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
			    dto.getEmployeeId(), InsuranceStatus.ACTIVE))
		{
			throw new RuntimeException(
					"Employee already has an active insurance plan");
		}
		
		//all check passed
		
		EmployeeInsurance insurance = new EmployeeInsurance();
		insurance.setEmployee(employee);
		insurance.setInsurancePlan(plan);
		insurance.setAssignedDate(LocalDate.now());
		insurance.setExpiryDate(dto.getExpiryDate());
		insurance.setStatus(InsuranceStatus.ACTIVE);
		
		
		EmployeeInsurance saved = employeeInsuranceRepository.save(insurance);
		return mapToInsuranceResponse(saved);		
	
	}
	
	

	@Override
	public EmployeeInsuranceResponseDTO getEmployeeInsurance(Long employeeId) {

		EmployeeInsurance insurance = employeeInsuranceRepository
				.findByEmployee_UserIdAndStatus(employeeId, InsuranceStatus.ACTIVE)
				.orElseThrow(()-> new RuntimeException(
						"No active insurance found for employee: "+ employeeId));
	
		return mapToInsuranceResponse(insurance);
	}
	
	
	
	//MAPPERS- converts entity to response DTO 
=======
    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    @Autowired
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public InsurancePlanResponseDTO createInsurancePlan(
            CreateInsurancePlanRequestDTO dto, String createdBy) {

        if (insurancePlanRepository.existsByPlanName(dto.getPlanName())) {
            throw new ConflictException(
                    "Insurance plan with this name already exists");
        }

        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName(dto.getPlanName());
        plan.setCoverageAmount(dto.getCoverageAmount());
        plan.setDescription(dto.getDescription());
        plan.setCreatedBy(createdBy);
        plan.setIsActive(true);

        InsurancePlan saved = insurancePlanRepository.save(plan);
        return mapToPlanResponse(saved);
    }

    @Override
    public List<InsurancePlanResponseDTO> getAllInsurancePlans() {
        return insurancePlanRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToPlanResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateInsurancePlan(Long planId) throws ResourceNotFoundException {
        InsurancePlan plan = insurancePlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insurance plan not found with id: " + planId));

        plan.setIsActive(false);
        insurancePlanRepository.save(plan);
    }

    @Override
    public EmployeeInsuranceResponseDTO assignInsurance(AssignInsuranceRequestDTO dto) throws ResourceNotFoundException {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + dto.getEmployeeId()));

        InsurancePlan plan = insurancePlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insurance plan not found with id: " + dto.getPlanId()));

        if (!plan.getIsActive()) {
            throw new BusinessRuleException(
                    "Cannot assign a deactivated insurance plan");
        }

        if (employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
                dto.getEmployeeId(), InsuranceStatus.ACTIVE)) {
            throw new ConflictException(
                    "Employee already has an active insurance plan");
        }

        EmployeeInsurance insurance = new EmployeeInsurance();
        insurance.setEmployee(employee);
        insurance.setInsurancePlan(plan);
        insurance.setAssignedDate(LocalDate.now());
        insurance.setExpiryDate(dto.getExpiryDate());
        insurance.setStatus(InsuranceStatus.ACTIVE);

        EmployeeInsurance saved = employeeInsuranceRepository.save(insurance);
        return mapToInsuranceResponse(saved);
    }

    @Override
    public EmployeeInsuranceResponseDTO getEmployeeInsurance(Long employeeId) throws ResourceNotFoundException {
        EmployeeInsurance insurance = employeeInsuranceRepository
                .findByEmployee_UserIdAndStatus(employeeId, InsuranceStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active insurance found for employee: " + employeeId));

        return mapToInsuranceResponse(insurance);
    }

>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    private InsurancePlanResponseDTO mapToPlanResponse(InsurancePlan plan) {
        InsurancePlanResponseDTO dto = new InsurancePlanResponseDTO();
        dto.setPlanId(plan.getId());
        dto.setPlanName(plan.getPlanName());
        dto.setCoverageAmount(plan.getCoverageAmount());
        dto.setDescription(plan.getDescription());
        dto.setIsActive(plan.getIsActive());
        dto.setCreatedBy(plan.getCreatedBy());
        dto.setCreatedAt(plan.getCreatedAt());
        return dto;
    }

<<<<<<< HEAD
    private EmployeeInsuranceResponseDTO mapToInsuranceResponse(
            EmployeeInsurance ins) {
        EmployeeInsuranceResponseDTO dto = new EmployeeInsuranceResponseDTO();
        dto.setEmployeInsuranceId(ins.getId());
        dto.setEmployeeId(ins.getEmployee().getUserId());
        dto.setEmployeeName(ins.getEmployee().getFirstName()+" "+ins.getEmployee().getLastName());
=======
    private EmployeeInsuranceResponseDTO mapToInsuranceResponse(EmployeeInsurance ins) {
        EmployeeInsuranceResponseDTO dto = new EmployeeInsuranceResponseDTO();
        dto.setEmployeInsuranceId(ins.getId());
        dto.setEmployeeId(ins.getEmployee().getUserId());

        String firstName = ins.getEmployee().getFirstName();
        String lastName  = ins.getEmployee().getLastName();
        dto.setEmployeeName(
            (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank())
                ? firstName + " " + lastName
                : ins.getEmployee().getUsername()
        );

>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
        dto.setPlanName(ins.getInsurancePlan().getPlanName());
        dto.setCoverageAmount(ins.getInsurancePlan().getCoverageAmount());
        dto.setAssignedDate(ins.getAssignedDate());
        dto.setExpiryDate(ins.getExpiryDate());
        dto.setStatus(ins.getStatus());
        dto.setCreatedAt(ins.getCreatedAt());
        return dto;
    }
<<<<<<< HEAD


}
=======
}
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
