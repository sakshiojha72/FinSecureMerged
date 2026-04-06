package com.ds.app.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
import com.ds.app.dto.*;

@Service
public interface EmployeeSearchService {
	
	
	   // Search & Reports
    PagedResponseDTO<EmployeeResponseDTO> filterUsers(EmployeeFilterRequestDTO dto, Pageable pageable);
   
    
    PagedResponseDTO<EmployeeFinanceViewDTO> getEmployeesWithFinanceDetails(Pageable pageable);

    CountReportDTO getCountReport();
    
    // Returns employees who joined in last N days
    // days=30 means last 30 days, days=7 means last 7 days
    PagedResponseDTO<EmployeeResponseDTO> getRecentlyJoined( int days, Pageable pageable);
    
    PagedResponseDTO<EmployeeResponseDTO> getEmployeesWithoutPhoto(Pageable pageable);
    
    PagedResponseDTO<EmployeeResponseDTO> getIncompleteProfiles(Pageable pageable);
    
    
    
	 // Monthly stats for a given year
	 // HR passes year=2024 to see all 12 months of 2024
	 List<MonthlyStatDTO> getMonthlyStats(int year);
	  
	 // Yearly stats — all years
	 // HR sees hiring trend year by year
	 List<YearlyStatDTO> getYearlyStats();


}
