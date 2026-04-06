package com.ds.app.service;

import org.springframework.data.domain.Pageable;

import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.dto.response.PagedResponseDTO;
import com.ds.app.exception.EmployeeNotFoundException;


public interface EmployeeAdminService {
	
	
	

    // HR / Admin
	   
    void softDeleteEmployee(Long userId) throws Exception;

 
    // View all locked employees
//    PagedResponseDTO<EmployeeResponseDTO> findLockedEmployees(Pageable pageable);
    
 // Add this method
    void restoreEmployee(Long userId) throws EmployeeNotFoundException;
    
 // View all deleted employees
    PagedResponseDTO<EmployeeResponseDTO> findDeletedEmployees(Pageable pageable);
}
