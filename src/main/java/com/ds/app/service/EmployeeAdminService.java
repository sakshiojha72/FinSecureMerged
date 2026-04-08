package com.ds.app.service;

import org.springframework.data.domain.Pageable;

import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.dto.response.PagedResponseDTO;
import com.ds.app.exception.EmployeeNotFoundException;


public interface EmployeeAdminService {
	
	    void softDeleteEmployee(Long userId) throws Exception;
	
	    void restoreEmployee(Long userId) throws EmployeeNotFoundException;
	    
	    PagedResponseDTO<EmployeeResponseDTO> findDeletedEmployees(Pageable pageable);
    
}//endclass
