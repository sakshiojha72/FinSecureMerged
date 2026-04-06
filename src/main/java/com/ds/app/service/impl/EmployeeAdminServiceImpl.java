package com.ds.app.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.dto.response.PagedResponseDTO;
import com.ds.app.entity.Employee;
import com.ds.app.exception.EmployeeNotFoundException;
import com.ds.app.jwtutil.MaskingUtil;
import com.ds.app.repository.IEmployeeRepository;
import com.ds.app.repository.iAppUserRepository;
import com.ds.app.service.EmployeeAdminService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeAdminServiceImpl  implements EmployeeAdminService{
	
	@Autowired
	IEmployeeRepository iEmployeeRepo;
	
	@Autowired
	iAppUserRepository appUserRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeePhotoServiceImpl.class);



	@Override
	public void softDeleteEmployee(Long userId) throws Exception {
			 logger.warn("Soft delete for userId: {}", userId);
		        Employee employee = iEmployeeRepo.findByUserId(userId)
		                .orElseThrow(() -> new EmployeeNotFoundException(userId));
		        if (Boolean.TRUE.equals(employee.getIsDeleted())) {
		            logger.info("userId: {} already deleted — no action", userId);
		            return;
		        }
		        employee.setIsDeleted(true);
		        iEmployeeRepo.save(employee);
		        logger.info("Soft deleted userId: {}", userId);
		
	}
	
	// ── Private helper ─────────────────────────────────────────────────
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
       dto.setDepartment(e.getDepartment());
       dto.setDesignation(e.getDesignation());
       dto.setIsDeleted(e.getIsDeleted());
       dto.setIsAccountLocked(e.getIsAccountLocked());
       dto.setIsEscalated(e.getIsEscalated());
       dto.setCreatedAt(e.getCreatedAt());
       dto.setUpdatedAt(e.getUpdatedAt());
       return dto;
   }
    
    @Override
    @Transactional
    public void restoreEmployee(Long userId) throws EmployeeNotFoundException {
        logger.info("Restoring employee for userId: {}", userId);

        // Find employee — including deleted ones
        // Note: NOT using findByUserIdAndIsDeletedFalse
        // because we want to find deleted employees
        Employee employee = iEmployeeRepo.findByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFoundException(userId));

        // Check if actually deleted
        if (Boolean.FALSE.equals(employee.getIsDeleted())) {
            logger.info("Employee is not deleted for userId: {}", userId);
            throw new RuntimeException(
                    "Employee is not deleted for userId: " + userId);
        }

        // Restore
        employee.setIsDeleted(false);
        iEmployeeRepo.save(employee);

        logger.info("Employee restored for userId: {}", userId);
    }
    
    
    @Override
    @Transactional
    public PagedResponseDTO<EmployeeResponseDTO> findDeletedEmployees(
            Pageable pageable) {
        logger.info("Fetching deleted employees");

        Page<Employee> page = iEmployeeRepo.findByIsDeletedTrue(pageable);

        List<EmployeeResponseDTO> data = page.getContent()
                .stream()
                .map(this::mapToMaskedResponseDTO)
                .toList();

        logger.info("Found {} deleted employees", data.size());
        return PagedResponseDTO.of(data, page);
    }

}
