package com.ds.app.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
import com.ds.app.dto.*;
import com.ds.app.entity.Employee;
import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.jwtutil.MaskingUtil;
import com.ds.app.repository.IEmployeeRepository;
import com.ds.app.repository.iAppUserRepository;
import com.ds.app.service.EmployeeSearchService;
import com.ds.app.service.FinanceService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeSearchServiceImpl implements EmployeeSearchService {
	
	@Autowired
	IEmployeeRepository iEmployeeRepo;
	
	@Autowired
	iAppUserRepository appUserRepository;
	
	@Autowired
	FinanceService financeService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeSearchServiceImpl.class);

	@Override
	public PagedResponseDTO<EmployeeResponseDTO> filterUsers(EmployeeFilterRequestDTO dto, Pageable pageable) {
		logger.info("Filtering employees — filter: {}", dto);
		 
        Page<Employee> page = iEmployeeRepo.filterEmployees(
                dto.getFirstName(),
                dto.getDepartment(),
                dto.getDesignation(),
                dto.getEmploymentType(),
                dto.getEmployeeExperience(),
                dto.getIsDeleted(),
                dto.getIsAccountLocked(),
                pageable);
 
        List<EmployeeResponseDTO> data = page.getContent()
                .stream()
                .map(this::mapToMaskedResponseDTO)
                .toList();
 
        logger.info("Filter returned {} results", data.size());
        return PagedResponseDTO.of(data, page);
	}

	@Override
	public PagedResponseDTO<EmployeeFinanceViewDTO> getEmployeesWithFinanceDetails(Pageable pageable) {

        logger.info("Finance report requested");
 
        Page<Employee> page = iEmployeeRepo.findAllActiveEmployees(pageable);
 
        List<EmployeeFinanceViewDTO> data = page.getContent()
                .stream()
                .map(this::mapToFinanceViewDTO)
                .toList();
 
        return PagedResponseDTO.of(data, page);
	}

	  private EmployeeResponseDTO mapToMaskedResponseDTO(Employee e) {
	        EmployeeResponseDTO dto = new EmployeeResponseDTO();
	        dto.setUserId(e.getUserId());
	        dto.setUsername(e.getUsername());
	        dto.setRole(e.getRole());
	        dto.setEmployeeCode(e.getEmployeeCode());
	        dto.setFirstName(e.getFirstName());
	        dto.setLastName(e.getLastName());
	        dto.setEmail(MaskingUtil.maskEmail(e.getEmail()));
	        dto.setPhoneNumber(MaskingUtil.maskPhone(e.getPhoneNumber()));
	        dto.setDateOfBirth(e.getDateOfBirth());
	        dto.setGender(e.getGender());
	        dto.setDepartment(e.getDepartment());
	        dto.setDesignation(e.getDesignation());
	        dto.setJoiningDate(e.getJoiningDate());
	        dto.setEmploymentType(e.getEmploymentType());
	        dto.setCertificationStatus(e.getCertificationStatus());
	        dto.setCertificationName(e.getCertificationName());
	        dto.setAddressLine(e.getAddressLine());
	        dto.setCity(e.getCity());
	        dto.setState(e.getState());
	        dto.setCountry(e.getCountry());
	        dto.setPincode(e.getPincode());
	        dto.setIsDeleted(e.getIsDeleted());
	        dto.setIsAccountLocked(e.getIsAccountLocked());
	        dto.setIsEscalated(e.getIsEscalated());
	        dto.setProfilePhotoUrl(e.getProfilePhotoUrl());
	        dto.setCreatedAt(e.getCreatedAt());
	        dto.setUpdatedAt(e.getUpdatedAt());
	        dto.setIsProfileComplete(isProfileComplete(e));
	        return dto;
	    }
	 
	  private Boolean isProfileComplete(Employee e) {
		    return e.getFirstName()   != null && !e.getFirstName().isBlank()
		        && e.getLastName()    != null && !e.getLastName().isBlank()
		        && e.getEmail()       != null && !e.getEmail().isBlank()
		        && e.getPhoneNumber() != null && !e.getPhoneNumber().isBlank()
		        && e.getDateOfBirth() != null
		        && e.getGender()      != null
		        && e.getAddressLine() != null && !e.getAddressLine().isBlank()
		        && e.getCity()        != null && !e.getCity().isBlank()
		        && e.getState()       != null && !e.getState().isBlank()
		        && e.getCountry()     != null && !e.getCountry().isBlank()
		        && e.getPincode()     != null && !e.getPincode().isBlank();
		}
	  
	  
	  private EmployeeFinanceViewDTO mapToFinanceViewDTO(Employee e) {
	        return new EmployeeFinanceViewDTO(
	                mapToMaskedResponseDTO(e),
	                financeService.getBankByUserId(e.getUserId()),
	                financeService.getInvestmentByUserId(e.getUserId())
	        );
	    }

	  @Override
	  public CountReportDTO getCountReport() {
		  logger.info("Generating count report");
		  
	        CountReportDTO report = CountReportDTO.builder()
	 
	                // Overall
	                .totalActive(
	                        iEmployeeRepo.countByIsDeletedFalse())
	                .totalDeleted(
	                        iEmployeeRepo.countByIsDeletedTrue())
	 
	                // Experience
	                .totalFreshers(
	                        iEmployeeRepo.countByEmployeeExperience(
	                                EmployeeExperience.FRESHER))
	                .totalExperienced(
	                        iEmployeeRepo.countByEmployeeExperience(
	                                EmployeeExperience.EXPERIENCED))
	 
	                // Certification
	                .totalCertified(
	                        iEmployeeRepo.countByCertificationStatus(
	                                CertificationStatus.CERTIFIED))
	                .totalNonCertified(
	                        iEmployeeRepo.countByCertificationStatus(
	                                CertificationStatus.NON_CERTIFIED))

	                .build();
	 
	        logger.info("Count report: active={}, deleted={}, freshers={}, " +
	                    "experienced={}, certified={}, nonCertified={}, expired={}",
	                report.getTotalActive(),
	                report.getTotalDeleted(),
	                report.getTotalFreshers(),
	                report.getTotalExperienced(),
	                report.getTotalCertified(),
	                report.getTotalNonCertified());
	 
	        return report;
	  }

	  @Override
	  public PagedResponseDTO<EmployeeResponseDTO> getRecentlyJoined(int days, Pageable pageable) {
		  logger.info("Fetching employees joined in last {} days", days);
		  
	        // Calculate start date
	        LocalDate fromDate = LocalDate.now().minusDays(days);
	        logger.info("Fetching employees joined since: {}", fromDate);
	 
	        Page<Employee> page = iEmployeeRepo.findRecentlyJoined(
	                fromDate, pageable);
	 
	        List<EmployeeResponseDTO> data = page.getContent()
	                .stream()
	                .map(this::mapToMaskedResponseDTO)
	                .toList();
	 
	        logger.info("Found {} employees joined since {}",
	                data.size(), fromDate);
	 
	        return PagedResponseDTO.of(data, page);
	  }

	  @Override
	  @Transactional
	  public PagedResponseDTO<EmployeeResponseDTO> getEmployeesWithoutPhoto(Pageable pageable) {
		  
		 
		      Page<Employee> page = iEmployeeRepo
		              .findEmployeesWithoutPhoto(pageable);
		      List<EmployeeResponseDTO> data = page.getContent()
		              .stream()
		              .map(this::mapToMaskedResponseDTO)
		              .toList();
		      return PagedResponseDTO.of(data, page);
		  }

	  @Override
	  @Transactional
	  public PagedResponseDTO<EmployeeResponseDTO> getIncompleteProfiles(Pageable pageable) {
		  logger.info("Fetching employees with incomplete profiles");

		    Page<Employee> page =
		            iEmployeeRepo.findIncompleteProfiles(pageable);

		    List<EmployeeResponseDTO> data = page.getContent()
		            .stream()
		            .map(this::mapToMaskedResponseDTO)
		            .toList();

		    logger.info("Found {} employees with incomplete profiles",
		            data.size());

		    return PagedResponseDTO.of(data, page);

	  }

	  @Override
	  @Transactional
	  public List<MonthlyStatDTO> getMonthlyStats(int year) {
	      logger.info("Fetching monthly stats for year: {}", year);
	   
	      List<Object[]> results =
	              iEmployeeRepo.countByMonthAndYear(year);
	   
	      // Build map of month → count from DB results
	      Map<Integer, Long> monthCountMap = new HashMap<>();
	      for (Object[] row : results) {
	          int month = ((Number) row[0]).intValue();
	          long count = ((Number) row[1]).longValue();
	          monthCountMap.put(month, count);
	      }
	   
	      // Build list for all 12 months
	      // Months with no joinings show count = 0
	      List<MonthlyStatDTO> stats = new ArrayList<>();
	      String[] monthNames = {
	          "January", "February", "March", "April",
	          "May", "June", "July", "August",
	          "September", "October", "November", "December"
	      };
	   
	      for (int i = 1; i <= 12; i++) {
	          stats.add(MonthlyStatDTO.builder()
	                  .year(year)
	                  .month(i)
	                  .monthName(monthNames[i - 1])
	                  .count(monthCountMap.getOrDefault(i, 0L))
	                  .build());
	      }
	   
	      logger.info("Monthly stats for {}: {} months returned",
	              year, stats.size());
	      return stats;
	  }
	   

	  //   Returns how many employees joined each year.
	  //   No year parameter needed — returns all years.
	  //   Sorted by year DESC — most recent year first.
	  //   HR uses this to see overall hiring trend.
	  @Override
	  @Transactional
	  public List<YearlyStatDTO> getYearlyStats() {
	      logger.info("Fetching yearly stats");
	   
	      List<Object[]> results = iEmployeeRepo.countByYear();
	   
	      List<YearlyStatDTO> stats = results.stream()
	    		  .filter(row -> row[0] != null) 
	              .map(row -> YearlyStatDTO.builder()
	                      .year(((Number) row[0]).intValue())
	                      .count(((Number) row[1]).longValue())
	                      .build())
	              .collect(Collectors.toList());
	   
	      logger.info("Yearly stats: {} years returned", stats.size());
	      return stats;
	      
	  }
	

}//end class
