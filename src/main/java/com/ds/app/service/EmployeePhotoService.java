package com.ds.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ds.app.dto.response.ProfilePhotoResponseDTO;

@Service
public interface EmployeePhotoService {
	


	 
	    ProfilePhotoResponseDTO uploadProfilePhoto(MultipartFile file, String username) throws Exception;

	   
	 
	 
	    
	    
}//end class
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	 
	 
	 
		// FILE: EmployeeService.java
		//
		// Interface declaring all service methods for the User Profile module.
		// Implemented by EmployeeServiceImpl.
		//
		// METHOD GROUPING:
		//
		//   STEP 2 — HR creates employee
//		     createEmployeeByHR()         → HR creates AppUser + Employee row
		//
		//   STEP 3/4 — Employee self-service
//		     getOwnProfile()              → Employee views own profile
//		     updateOwnProfile()           → Employee fills personal info (Step 4)
//		     uploadProfilePhoto()         → Employee uploads photo
		//
		//   HR / Admin management
//		     getEmployeeById()            → HR views any employee by userId
//		     updateEmployeeByHr()         → HR updates professional fields
//		     softDeleteEmployee()         → HR/Admin soft deletes an employee
		//
		//   Search & Reports (Pageable passed from controller — NOT built in service)
//		     filterUsers()                → Multi-criteria paginated search
//		     getEmployeesWithFinanceDetails() → Finance report with bank/investment
		// ════════════════════════════════════════════════════════════════════════════
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

