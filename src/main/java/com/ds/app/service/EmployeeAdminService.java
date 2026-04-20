package com.ds.app.service;

import org.springframework.data.domain.Pageable;

import com.ds.app.dto.response.EmployeeProfileResponseDTO;
import com.ds.app.dto.response.PagedResponseDTO;
import com.ds.app.exception.EmployeeNotFoundException1;


public interface EmployeeAdminService {
	
	    void softDeleteEmployee(Long userId) throws Exception;
	
	    void restoreEmployee(Long userId) throws EmployeeNotFoundException1;
	    
	    PagedResponseDTO<EmployeeProfileResponseDTO> findDeletedEmployees(Pageable pageable);
    
}//endclass
