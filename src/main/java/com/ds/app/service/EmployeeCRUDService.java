package com.ds.app.service;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;

public interface EmployeeCRUDService {
	
	
	HRCreateEmployeeResponseDTO createEmployeeByHR(HRCreateEmployeeRequestDTO dto, String hrUsername) throws Exception;

    EmployeeResponseDTO getOwnProfile(String username) throws Exception;

    EmployeeResponseDTO updateOwnProfile(EmployeeUpdateRequestDTO dto, String username) throws Exception;

    EmployeeResponseDTO getEmployeeById(Long userId) throws Exception;
    
	EmployeeResponseDTO updateEmployeeByHr(Long userId, EmployeeHRUpdateDTO dto, String hrUsername) throws Exception;
	
	EmployeeDashboardDTO getEmployeeDashboard(String username) throws Exception;
	
	EmployeeExportDTO exportEmployeeProfile(Long userId) throws Exception;
	
    
}//endclass
