package com.ds.app.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;

public interface EmployeeSearchService {
	
    PagedResponseDTO<EmployeeResponseDTO> filterUsers(EmployeeFilterRequestDTO dto, Pageable pageable);
   
    CountReportDTO getCountReport();

    PagedResponseDTO<EmployeeResponseDTO> getRecentlyJoined( int days, Pageable pageable);
    
    PagedResponseDTO<EmployeeResponseDTO> getEmployeesWithoutPhoto(Pageable pageable);
    
    PagedResponseDTO<EmployeeResponseDTO> getIncompleteProfiles(Pageable pageable);
 
	List<MonthlyStatDTO> getMonthlyStats(int year);

	List<YearlyStatDTO> getYearlyStats();


}//end class
