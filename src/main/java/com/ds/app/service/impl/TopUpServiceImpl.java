package com.ds.app.service.impl;
 
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.ds.app.dto.request.BuyTopUpRequestDTO;
import com.ds.app.dto.request.CreateTopUpPlanRequestDTO;
import com.ds.app.dto.response.EmployeeTopUpResponseDTO;
import com.ds.app.dto.response.TopUpPlanResponseDTO;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeTopUp;
import com.ds.app.entity.TopUpPlan;
import com.ds.app.enums.InsuranceStatus;
import com.ds.app.exception.BusinessRuleException;
import com.ds.app.exception.ConflictException;
import com.ds.app.exception.ResourceNotFoundException;
import com.ds.app.exception.ResourceNotFoundException2;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.EmployeeTopUpRepository;
import com.ds.app.repository.TopUpPlanRepository;
import com.ds.app.service.TopUpService;
 
@Service
public class TopUpServiceImpl implements TopUpService {
 
    @Autowired
    private TopUpPlanRepository topUpPlanRepository;
 
    @Autowired
    private EmployeeTopUpRepository employeeTopUpRepository;
 
    @Autowired
    private EmployeeRepository employeeRepository;
 
    @Autowired
    private EmployeeInsuranceRepository employeeInsuranceRepository;
 
    @Override
    public TopUpPlanResponseDTO createTopUpPlan(
            CreateTopUpPlanRequestDTO dto, String createdBy) {
 
        if (topUpPlanRepository.existsByTopUpName(dto.getTopUpName())) {
            throw new ConflictException(
                    "Top-up plan with this name already exists");
        }
 
        TopUpPlan topUp = new TopUpPlan();
        topUp.setTopUpName(dto.getTopUpName());
        topUp.setAdditionalCoverage(dto.getAdditionalCoverage());
        topUp.setPrice(dto.getPrice());
        topUp.setDescription(dto.getDescription());
        topUp.setCreatedBy(createdBy);
        topUp.setIsActive(true);
 
        TopUpPlan saved = topUpPlanRepository.save(topUp);
        return mapToTopUpPlanResponse(saved);
    }
 
    @Override
    public List<TopUpPlanResponseDTO> getAllTopUpPlans() {
        return topUpPlanRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToTopUpPlanResponse)
                .collect(Collectors.toList());
    }
 
    @Override
    public void deactivateTopUpPlan(Long topUpPlanId) {
        TopUpPlan topUp = topUpPlanRepository.findById(topUpPlanId)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Top-up plan not found with id: " + topUpPlanId));
 
        topUp.setIsActive(false);
        topUpPlanRepository.save(topUp);
    }
 
    @Override
    public EmployeeTopUpResponseDTO buyTopUp(BuyTopUpRequestDTO dto, Long employeeId) {
 
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Employee not found"));
 
        TopUpPlan topUpPlan = topUpPlanRepository.findById(dto.getTopUpPlanId())
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Top-up plan not found"));
 
        if (!topUpPlan.getIsActive()) {
            throw new BusinessRuleException(
                    "This top-up plan is no longer available for purchase");
        }
 
        if (!employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
                employeeId, InsuranceStatus.ACTIVE)) {
            throw new BusinessRuleException(
                    "Employee must have an active base insurance before buying a top-up");
        }
 
        if (employeeTopUpRepository.existsByEmployee_UserIdAndTopUpPlan_Id(
                employeeId, dto.getTopUpPlanId())) {
            throw new ConflictException(
                    "Employee has already purchased this top-up plan");
        }
 
        EmployeeTopUp purchase = new EmployeeTopUp();
        purchase.setEmployee(employee);
        purchase.setTopUpPlan(topUpPlan);
        purchase.setPurchasedDate(LocalDate.now());
        purchase.setExpiryDate(dto.getExpiryDate());
        purchase.setStatus(InsuranceStatus.ACTIVE);
 
        EmployeeTopUp saved = employeeTopUpRepository.save(purchase);
        return mapToTopUpResponse(saved);
    }
 
    @Override
    public List<EmployeeTopUpResponseDTO> getEmployeeTopUps(Long employeeId) {
        return employeeTopUpRepository.findByEmployee_UserId(employeeId)
                .stream()
                .map(this::mapToTopUpResponse)
                .collect(Collectors.toList());
    }
 
    private TopUpPlanResponseDTO mapToTopUpPlanResponse(TopUpPlan plan) {
        TopUpPlanResponseDTO dto = new TopUpPlanResponseDTO();
        dto.setTopUpPlanId(plan.getId());
        dto.setTopUpName(plan.getTopUpName());
        dto.setAdditionalCoverage(plan.getAdditionalCoverage());
        dto.setPrice(plan.getPrice());
        dto.setDescription(plan.getDescription());
        dto.setIsActive(plan.getIsActive());
        dto.setCreatedAt(plan.getCreatedAt());
        return dto;
    }
 
    private EmployeeTopUpResponseDTO mapToTopUpResponse(EmployeeTopUp topUp) {
        EmployeeTopUpResponseDTO dto = new EmployeeTopUpResponseDTO();
        dto.setEmployeeTopUpId(topUp.getId());
        dto.setEmployeeId(topUp.getEmployee().getUserId());
 
        String firstName = topUp.getEmployee().getFirstName();
        String lastName  = topUp.getEmployee().getLastName();
        dto.setEmployeeName(
            (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank())
                ? firstName + " " + lastName
                : topUp.getEmployee().getUsername()
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