package com.ds.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.Employee;
import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.EmploymentType;


@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
	
	    // ── Single record lookups

	    // Used by: HR to fetch employee by userId after search
	    Optional<Employee> findByUserId(Long userId);

	    // Used by: service layer to resolve username from JWT token
	    Optional<Employee> findByUsername(String username);

	    // Used by: HR module if looking up by employeeCode
	    Employee findByEmployeeCode(String employeeCode);

	    // ── Existence checks (used in service for validation)
	    // Used by: createEmployeeByHR — prevent duplicate userIds
	    boolean existsByUserId(Long userId);

	    // Used by: createEmployeeByHR, updateOwnProfile — email uniqueness
	    boolean existsByEmail(String email);

	    // Used by: createEmployeeByHR, updateOwnProfile — phone uniqueness
	    boolean existsByPhoneNumber(String phoneNumber);
	    
	    boolean existsByUsername(String username);

	    // Used by: employeeCode auto-generation collision guard
	    boolean existsByEmployeeCode(String employeeCode);

	    // Used by: updateOwnProfile — allow same user to keep their email
	    boolean existsByEmailAndUserIdNot(String email, Long userId);

	    // Used by: updateOwnProfile — allow same user to keep their phone
	    boolean existsByPhoneNumberAndUserIdNot(String phoneNumber, Long userId);


	    // Used by: getEmployeeById, updateEmployeeByHr — skip deleted records
	    Optional<Employee> findByUserIdAndIsDeletedFalse(Long userId);

	    // Used by: default list queries — never show deleted employees
	    Page<Employee> findAllByIsDeletedFalse(Pageable pageable);
	    
	 // Find all deleted employees
	    Page<Employee> findByIsDeletedTrue(Pageable pageable);

	    // When a param is null, its condition is skipped in the WHERE clause.
	    // Pageable controls page number, size, and sort — passed from controller.

	    @Query("""
	            SELECT e FROM Employee e
	            WHERE (:firstName       IS NULL OR LOWER(e.firstName)        LIKE LOWER(CONCAT('%', :firstName, '%')))
	              AND (:department      IS NULL OR LOWER(e.department)       LIKE LOWER(CONCAT('%', :department, '%')))
	              AND (:designation     IS NULL OR LOWER(e.designation)      LIKE LOWER(CONCAT('%', :designation, '%')))
	              AND (:employmentType  IS NULL OR e.employmentType          = :employmentType)
	              AND (:employeeExp     IS NULL OR e.employeeExperience      = :employeeExp)
	              AND (:isDeleted       IS NULL OR e.isDeleted               = :isDeleted)
	              AND (:isAccountLocked IS NULL OR e.isAccountLocked         = :isAccountLocked)""")
	    
	    Page<Employee> filterEmployees(
	            @Param("firstName")       String firstName,
	            @Param("department")      String department,
	            @Param("designation")     String designation,
	            @Param("employmentType")  EmploymentType employmentType,
	            @Param("employeeExp")     EmployeeExperience employeeExperience,
	            @Param("isDeleted")       Boolean isDeleted,
	            @Param("isAccountLocked") Boolean isAccountLocked,
	            Pageable pageable);

	    // ── Finance report — fetch all active employees ────────────────────
	    // Used by: getEmployeesWithFinanceDetails — bank + investment report

	    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false")
	    Page<Employee> findAllActiveEmployees(Pageable pageable);

	    // ── Count helpers for dashboard reports ───────────────────────────

	    long countByIsDeletedFalse();
	    long countByIsEscalatedTrue();
	    long countByIsAccountLockedTrue();

		boolean existsByEmployeeCodeAndUserIdNot(String employeeCode, Long userId);

		Page<Employee> findByIsAccountLockedTrue(Pageable pageable);
	

		long countByIsDeletedTrue();

		long countByEmployeeExperience(EmployeeExperience experience);
		
		@Query("SELECT e FROM Employee e " +
			       "WHERE e.profilePhotoUrl IS NULL " +
			       "AND e.isDeleted = false")
			Page<Employee> findEmployeesWithoutPhoto(Pageable pageable);

		long countByCertificationStatus(CertificationStatus status);
		
		 @Query("SELECT e FROM Employee e " +
		           "WHERE e.joiningDate >= :fromDate " +
		           "AND e.isDeleted = false " +
		           "ORDER BY e.joiningDate DESC")
		    Page<Employee> findRecentlyJoined( @Param("fromDate") LocalDate fromDate, Pageable pageable);
		 
		 @Query("SELECT e FROM Employee e " +
			       "WHERE e.isDeleted = false " +
			       "AND (" +
			            "e.firstName   IS NULL OR " +
			            "e.lastName    IS NULL OR " +
			            "e.email       IS NULL OR " +
			            "e.phoneNumber IS NULL OR " +
			            "e.dateOfBirth IS NULL OR " +
			            "e.gender      IS NULL OR " +
			            "e.addressLine IS NULL OR " +
			            "e.city        IS NULL OR " +
			            "e.state       IS NULL OR " +
			            "e.country     IS NULL OR " +
			            "e.pincode     IS NULL" + ")")
		 
			Page<Employee> findIncompleteProfiles(Pageable pageable);

			// Count of incomplete profiles — used in count report
			@Query("SELECT COUNT(e) FROM Employee e " +
			       "WHERE e.isDeleted = false " +
			       "AND (" +
			            "e.firstName   IS NULL OR " +
			            "e.lastName    IS NULL OR " +
			            "e.email       IS NULL OR " +
			            "e.phoneNumber IS NULL OR " +
			            "e.dateOfBirth IS NULL OR " +
			            "e.gender      IS NULL OR " +
			            "e.addressLine IS NULL OR " +
			            "e.city        IS NULL OR " +
			            "e.state       IS NULL OR " +
			            "e.country     IS NULL OR " +
			            "e.pincode     IS NULL" + ")")
			
			long countIncompleteProfiles();
			
			
			@Query("SELECT MONTH(e.joiningDate), COUNT(e) " +
				       "FROM Employee e " +
				       "WHERE YEAR(e.joiningDate) = :year " +
				       "AND e.isDeleted = false " +
				       "GROUP BY MONTH(e.joiningDate) " +
				       "ORDER BY MONTH(e.joiningDate) ASC")
				List<Object[]> countByMonthAndYear(@Param("year") int year);
				 
				// Yearly statistics — how many employees joined each year
				// Returns Object[] where:
				//   index 0 = year (2022, 2023, 2024)
				//   index 1 = count of employees
				@Query("SELECT YEAR(e.joiningDate), COUNT(e) " +
				       "FROM Employee e " +
				       "WHERE e.isDeleted = false " +
				       "GROUP BY YEAR(e.joiningDate) " +
				       "ORDER BY YEAR(e.joiningDate) DESC")
				
				List<Object[]> countByYear();

}//end class
