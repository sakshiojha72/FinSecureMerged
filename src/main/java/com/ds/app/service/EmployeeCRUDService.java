package com.ds.app.service;

import org.springframework.stereotype.Service;


import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
import com.ds.app.dto.*;


@Service
public interface EmployeeCRUDService {
	
	
	HRCreateEmployeeResponseDTO createEmployeeByHR(HRCreateEmployeeRequestDTO dto, String hrUsername) throws Exception;

  
    EmployeeResponseDTO getOwnProfile(String username) throws Exception;


    EmployeeResponseDTO updateOwnProfile(EmployeeUpdateRequestDTO dto, String username) throws Exception;

    EmployeeResponseDTO getEmployeeById(Long userId) throws Exception;
    
	EmployeeResponseDTO updateEmployeeByHr(Long userId, EmployeeHRUpdateDTO dto, String hrUsername) throws Exception;
	
	// Employee views their own dashboard summary
	EmployeeDashboardDTO getEmployeeDashboard(String username) throws Exception;
	
	 
	// HR exports complete profile of any employee
	EmployeeExportDTO exportEmployeeProfile(Long userId) throws Exception;
    
}
