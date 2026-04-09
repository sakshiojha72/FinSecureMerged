package com.ds.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.Employee;
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

}
