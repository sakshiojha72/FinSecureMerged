package com.ds.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.dto.response.PagedResponseDTO;
import com.ds.app.exception.EmployeeNotFoundException;
import com.ds.app.service.EmployeeAdminService;
import com.ds.app.service.impl.EmployeePhotoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/finsecure/admin")
@Validated
@Tag(name = "Admin Actions", description = "Privileged HR/Admin operations — soft delete. Future: restore, lock, unlock.")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeePhotoServiceImpl.class);
	
	@Autowired
	EmployeeAdminService employeeAdminService;
	
	//Soft delete
	   @DeleteMapping("/employee/{userId}")
	    @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	    @Operation(summary = "Soft delete employee (HR / Admin)",
	        description = "Sets isDeleted=true. Record retained in DB, excluded from normal queries. " +
	                      "Idempotent — safe to call twice. " +
	                      "Find userId via GET /finsecure/hr/employees/search first."
	    )
	    public ResponseEntity<String> softDeleteEmployee(@PathVariable Long userId) throws Exception {
		   
	        logger.warn("Soft delete for userId: {}", userId);
	        employeeAdminService.softDeleteEmployee(userId);
	        return ResponseEntity.ok("Employee deleted successfully");
	        
	    }
	   
	  // restore deleted 
	// PUT /finsecure/admin/employee/{userId}/restore
	   @PutMapping("/employee/{userId}/restore")
	   @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	   @Operation(summary = "Restore deleted employee (HR / Admin)",
	       description = "Sets isDeleted = false. " +
	                     "Employee can login and appear in searches again. " +
	                     "Find userId via GET /finsecure/admin/employees/deleted first."
	   )
	   public ResponseEntity<String> restoreEmployee(@PathVariable Long userId) throws EmployeeNotFoundException {
		   
	       logger.info("Restore request for userId: {}", userId);
	       employeeAdminService.restoreEmployee(userId);
	       return ResponseEntity.ok("Employee restored successfully for userId: " + userId);
	   }
	   
	   //view deleted
	// GET /finsecure/admin/employees/deleted
	   @GetMapping("/employees/deleted")
	   @PreAuthorize("hasAnyAuthority('HR', 'ADMIN')")
	   @Operation(summary = "Find all deleted employees (HR / Admin)",
	   description = "Returns paginated list of soft deleted employees. " +
	                     "Use returned userId to call restore endpoint.")
	   
	   public ResponseEntity<PagedResponseDTO<EmployeeResponseDTO>> findDeletedEmployees( @ParameterObject
			   @PageableDefault(page = 0, size = 10,
	                            sort = "userId", direction = Sort.Direction.DESC) Pageable pageable) {
		   
	       logger.info("Finding deleted employees");
	       return ResponseEntity.ok(employeeAdminService.findDeletedEmployees(pageable));
	   }
	

	@GetMapping("/test")
	public ResponseEntity<String> test(){
		return new ResponseEntity<String>("successfuly accessing admin controller",HttpStatus.OK);
	}
	

}//end class
