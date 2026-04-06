package com.ds.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/finsecure/employee")
@Validated
@Tag(name = "Employee", description = "Employee APIs")
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeePhotoServiceImpl.class);
	
	@Autowired
	EmployeePhotoService employeePhotoService;
	
	@Autowired
	EmployeeCRUDService employeeCRUDService;
	
	@Autowired
	EmployeeRewardService employeeRewardService;
	
	@Autowired
	IEmployeeRepository employeeRepository;
		

	@GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    @Operation(summary = "View own profile (STEP 3 — after first login)",
        description = "Employee views their profile. At this point HR-assigned fields " +
                      "(employeeCode, department, designation) are set. " +
                      "Personal fields (name, email, phone, address) are null until Step 4. " +
                      "No userId needed — resolved from JWT."
    )
    public ResponseEntity<EmployeeResponseDTO> getOwnProfile(@AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
        logger.info("GET own profile for: {}", userDetails.getUsername());
        return ResponseEntity.ok(employeeCRUDService.getOwnProfile(userDetails.getUsername()));
    }
			
	
	
	@PutMapping("/update/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    @Operation( summary = "Fill personal information (STEP 4 — onboarding completion)",
        description = "Employee fills in personal info after first login. " +
                      "Fields: firstName, lastName, email, phoneNumber, dateOfBirth, " +
                      "gender, addressLine, city, state, country, pincode. " +
                      "Employee CANNOT change: department, designation, certificationStatus (HR-controlled)."
    )
    public ResponseEntity<EmployeeResponseDTO> updateOwnProfile( @Valid @RequestBody EmployeeUpdateRequestDTO dto,@AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
        
		logger.info("Employee '{}' filling personal info", userDetails.getUsername());
        return ResponseEntity.ok(employeeCRUDService.updateOwnProfile(dto, userDetails.getUsername()));
    }
	
	
	  @PostMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
	    @Operation(summary = "Upload profile photo",
	        description = "Accepted: JPG, JPEG, PNG. Max 2MB. " +
	                      "Use multipart/form-data with field name 'file'. " +
	                      "Uploading again replaces the previous photo."
	    )
	    public ResponseEntity<ProfilePhotoResponseDTO> uploadProfilePhoto( @RequestParam("file") MultipartFile file, @AuthenticationPrincipal MyUserDetails userDetails) throws Exception {
		  
	        logger.info("Photo upload for: {}", userDetails.getUsername());
	        return ResponseEntity.ok(employeePhotoService.uploadProfilePhoto(file, userDetails.getUsername()));
	    }
	  
	  @GetMapping("/dashboard")
	  @PreAuthorize("hasAuthority('EMPLOYEE')")
	  @Operation(summary = "Employee dashboard (Employee only)", 
	  description = "Returns complete summary of employee's own profile.\n\n")
	  
	  public ResponseEntity<EmployeeDashboardDTO> getEmployeeDashboard( @AuthenticationPrincipal UserDetails userDetails) throws Exception {

	      logger.info("Dashboard for: {}", userDetails.getUsername());
	      return ResponseEntity.ok( employeeCRUDService.getEmployeeDashboard(userDetails.getUsername()));
	      
	  }
	  
	  // ── GET /finsecure/employee/rewards ───────────────────────────
	    @GetMapping("/rewards")//employee controller
	    @PreAuthorize("hasAuthority('EMPLOYEE')")
	    @Operation(
	        summary = "Get my rewards (Employee)",
	        description =
	            "Employee views all own rewards and recognitions.\n" +
	            "Sorted by reward date — most recent first.\n" +
	            "No userId needed — resolved from JWT."
	    )
	    public ResponseEntity<List<EmployeeRewardResponseDTO>> getMyRewards(@AuthenticationPrincipal UserDetails userDetails) {
	    	
	        Long userId = getUserId(userDetails.getUsername());
	        logger.info("Fetching rewards for: {}",
	                userDetails.getUsername());
	        return ResponseEntity.ok(
	                employeeRewardService.getMyRewards(userId));
	    }
	 
	    private Long getUserId(String username) {
	    	
	        return employeeRepository.findByUsername(username)
	                .orElseThrow(() -> new RuntimeException( "Employee not found: " + username))
	                .getUserId();
	    }




	@GetMapping("/test")
	public ResponseEntity<String> test() {
		
		return new ResponseEntity<String>("successfuly accessing employee controller",HttpStatus.OK);
	}
}//end class

