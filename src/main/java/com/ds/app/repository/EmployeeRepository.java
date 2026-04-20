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
import com.ds.app.enums.Status;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByUsername(String name);

	Optional<Employee> findByUserId(Long userId);

	boolean existsByUserId(Long userId);

	List<Employee> findByStatus(Status status);

	Page<Employee> findByBankAccountIsNull(Pageable pageable);

	boolean existsByUsername(String string);
	
	@Query("SELECT e FROM Employee e WHERE " +
	           "e.certificationStatus = 'NON_CERTIFIED' OR " +
	           "e.skillStatus = 'NON_SKILLED' OR " +
	           "e.employeeExperience = 'FRESHER'")
	    Page<Employee> findEligibleForTraining(Pageable pageable);
	
	@Query("""
		    SELECT e
		    FROM Employee e
		    WHERE
		      ( e.certificationStatus = 'NON_CERTIFIED'
		        OR e.skillStatus = 'NON_SKILLED'
		        OR e.employeeExperience = 'FRESHER'
		      )
		      AND e.department.id = :deptId
		""")
		Page<Employee> findEligibleByDepartment(
		        @Param("deptId") Long deptId,
		        Pageable pageable
		);
	
	 Employee findByEmployeeCode(String employeeCode);
	 
	 boolean existsByEmail(String email);

	 boolean existsByPhoneNumber(String phoneNumber);
	 
	 boolean existsByEmployeeCode(String employeeCode);
	   
	    boolean existsByEmailAndUserIdNot(String email, Long userId);
 
	    boolean existsByPhoneNumberAndUserIdNot(String phoneNumber, Long userId);

	    Optional<Employee> findByUserIdAndIsDeletedFalse(Long userId);
	   
	    Page<Employee> findAllByIsDeletedFalse(Pageable pageable);
	    
	    Page<Employee> findByIsDeletedTrue(Pageable pageable);


	    @Query("""
	            SELECT e FROM Employee e
	            WHERE (:firstName       IS NULL OR LOWER(e.firstName)        LIKE LOWER(CONCAT('%', :firstName, '%')))
	              AND (:designation     IS NULL OR LOWER(e.designation)      LIKE LOWER(CONCAT('%', :designation, '%')))
	              AND (:employmentType  IS NULL OR e.employmentType          = :employmentType)
	              AND (:employeeExp     IS NULL OR e.employeeExperience      = :employeeExp)
	              AND (:isDeleted       IS NULL OR e.isDeleted               = :isDeleted)
	              AND (:isAccountLocked IS NULL OR e.isAccountLocked         = :isAccountLocked)""")
	    
	    Page<Employee> filterEmployees(
	            @Param("firstName")       String firstName,
	            @Param("designation")     String designation,
	            @Param("employmentType")  EmploymentType employmentType,
	            @Param("employeeExp")     EmployeeExperience employeeExperience,
	            @Param("isDeleted")       Boolean isDeleted,
	            @Param("isAccountLocked") Boolean isAccountLocked,
	            Pageable pageable);

	 

	    @Query("SELECT e FROM Employee e WHERE e.isDeleted = false")
	    Page<Employee> findAllActiveEmployees(Pageable pageable);


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
				 
			
		@Query("SELECT YEAR(e.joiningDate), COUNT(e) " +
				       "FROM Employee e " +
				       "WHERE e.isDeleted = false " +
				       "GROUP BY YEAR(e.joiningDate) " +
				       "ORDER BY YEAR(e.joiningDate) DESC")
		List<Object[]> countByYear();

		Long countByProfilePhotoUrlIsNullAndIsDeletedFalse();
	 
	 
	
	

}
