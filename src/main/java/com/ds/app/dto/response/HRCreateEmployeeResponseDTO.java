package com.ds.app.dto.response;

import java.time.LocalDate;

import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.EmploymentType;
import com.ds.app.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HRCreateEmployeeResponseDTO {
	
	private Long userId;
	
	private String username;
	private String password;
	private String temporaryPassword; //HR emails this to employee
	
	private String employeeCode;
	private UserRole userRole;
	
	private String department;
	private String designation;
	
	private LocalDate dateOfJoining;
	  private EmploymentType employmentType;

	    private CertificationStatus certificationStatus;


	    private String certificationName;

	    private EmployeeExperience employeeExperience;
	    
	    private String message;
	    

}
