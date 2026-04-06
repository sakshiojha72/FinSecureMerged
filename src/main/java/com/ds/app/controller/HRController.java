package com.ds.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
import com.ds.app.dto.*;
import com.ds.app.entity.MyUserDetails;
import com.ds.app.repository.IEmployeeRepository;
import com.ds.app.service.EmployeeCRUDService;
import com.ds.app.service.EmployeePhotoService;
import com.ds.app.service.EmployeeRewardService;
import com.ds.app.service.impl.EmployeePhotoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/finsecure/hr")
public class HRController {
	
	@Autowired
	EmployeeCRUDService employeeCRUDService;
	
	@Autowired
	EmployeeRewardService employeeRewardService;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeePhotoServiceImpl.class);

	@GetMapping("/test")
	public ResponseEntity<String> test(){
		return new ResponseEntity<String>("successfuly accessing hr controller",HttpStatus.OK);
	}


	@PostMapping("/employee/create")
    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
    @Operation(summary = "Create employee account (STEP 2)",
        description = "HR creates the employee account. System auto-generates employeeCode. " +
                      "Response includes credentials for HR to forward to the employee. " +
                      "Personal info (name, email, phone) is NOT set here — employee fills it in Step 4."
    )
    public ResponseEntity<HRCreateEmployeeResponseDTO> createEmployee(
            @Valid @RequestBody HRCreateEmployeeRequestDTO dto,@AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
       
		logger.info("HR '{}' creating employee: {}", userDetails.getUsername(), dto.getUsername());
        return new ResponseEntity<>(employeeCRUDService.createEmployeeByHR(dto, userDetails.getUsername()), HttpStatus.CREATED);
    }
	
	
	
	@GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
    @Operation(summary = "Get employee by userId (HR / Admin)",
	        description = 
	        "Fetch any employee's full profile. " +  
	        "Use GET /finsecure/hr/employees/search first to find the userId."
    )
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long userId) throws Exception {
		
        logger.info("HR fetching employee for userId: {}", userId);
        return ResponseEntity.ok(employeeCRUDService.getEmployeeById(userId));
    }
	
	
	 @PutMapping("/update/employee/{userId}")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(
	        summary = "Update professional fields (HR / Admin)",
	        description = "Updates: department, designation, employmentType, certificationStatus, " +
	                      "certificationName, certificationExpiryDate, employeeExperience. " +
	                      "If certificationStatus = CERTIFIED, name and expiry date are required."
	    )
	    public ResponseEntity<EmployeeResponseDTO> updateEmployeeByHr(@PathVariable Long userId,@Valid @RequestBody EmployeeHRUpdateDTO dto,  
	            @AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
	       
		 logger.info("HR '{}' updating userId: {}", userDetails.getUsername(), userId);
	        return ResponseEntity.ok(employeeCRUDService.updateEmployeeByHr(userId, dto, userDetails.getUsername()));
	    }
	 
	 
	 @PostMapping("/employee/{userId}/rewards")
	 @PreAuthorize("hasAnyAuthority('HR','ADMIN')")
	 @Operation(summary = "Give reward to employee (HR/ADMIN)", description =
			 			"HR gives recognition/reward to an employee.\n\n" +
			            "RewardType: EMPLOYEE_OF_MONTH, BEST_PERFORMER, " +
			            "INNOVATION_AWARD, TEAM_PLAYER, LEADERSHIP_AWARD" +
			            "RewardCategory: PERFORMANCE, INNOVATION, " +
			            "TEAMWORK, LEADERSHIP, CLIENT_SATISFACTION, SPECIAL"
			    )
	 
	 public ResponseEntity<EmployeeRewardResponseDTO> giveReward(@PathVariable Long userId, @Valid @RequestBody EmployeeRewardRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails ) throws Exception{
		 
		 logger.info("HR '{}' giving reward to userId: {}", userDetails.getUsername(), userId);
	        return new ResponseEntity<>(employeeRewardService.giveReward(userId, dto, userDetails.getUsername()),HttpStatus.CREATED);

	 }
	 
	  // ── GET /finsecure/hr/employee/{userId}/rewards ───────────────
	    @GetMapping("/employee/{userId}/rewards")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(summary = "Get employee rewards (HR / Admin)",
	        description =
	            "HR views all rewards given to an employee.\n" +
	            "Sorted by reward date — most recent first."
	    )
	    public ResponseEntity<List<EmployeeRewardResponseDTO>> getRewardsByUserId(@PathVariable Long userId) {
	       
	    	logger.info("HR fetching rewards for userId: {}", userId);
	        return ResponseEntity.ok(employeeRewardService.getRewardsByUserId(userId));
	    
	    }
	    
	    

	    // ── PUT /finsecure/hr/rewards/{rewardId} ──────────────────────
	    @PutMapping("/rewards/{rewardId}")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(summary = "Update reward (HR / Admin)",
	        description =
	            "HR updates an existing reward.\n" +
	            "All fields optional — only provided fields updated.")
	    
	    public ResponseEntity<EmployeeRewardResponseDTO> updateReward(@PathVariable Integer rewardId,
	            @Valid @RequestBody EmployeeRewardRequestDTO dto) {
	    	
	        logger.info("Updating rewardId: {}", rewardId);
	        return ResponseEntity.ok(employeeRewardService.updateReward(rewardId, dto));
	    
	    }
	    
	    // ── DELETE /finsecure/hr/rewards/{rewardId} ───────────────────
	    @DeleteMapping("/rewards/{rewardId}")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(summary = "Delete reward (HR / Admin)",
	        description = 
	      "Permanently deletes a reward record.")
	    public ResponseEntity<String> deleteReward(@PathVariable Integer rewardId) {
	    	logger.warn("Deleting rewardId: {}", rewardId);
	        employeeRewardService.deleteReward(rewardId);
	        return ResponseEntity.ok("Reward deleted: " + rewardId);
	    }
	    

	    // ── GET /finsecure/hr/employees/top-rewarded ──────────────────
	    @GetMapping("/employees/top-rewarded")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(
	        summary = "Top rewarded employees (HR / Admin)",
	        description =
	            "Returns employees ranked by number of rewards.\n" +
	            "HR uses this for performance overview."
	    )
	    public ResponseEntity<List<Object[]>> getTopRewarded() {
	        logger.info("Top rewarded employees requested");
	        return ResponseEntity.ok(
	                employeeRewardService.getTopRewardedEmployees());
	    }


	
	
}//end class
