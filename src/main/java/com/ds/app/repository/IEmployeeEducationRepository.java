package com.ds.app.repository;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.ds.app.entity.EmployeeEducation;
 
@Repository
public interface IEmployeeEducationRepository extends JpaRepository<EmployeeEducation, Integer> {
 
    // All education records for an employee
    // sorted by passingYear DESC — most recent first
    //  WHERE employee.userId = ? ORDER BY passingYear DESC
    List<EmployeeEducation> findByEmployeeUserIdOrderByPassingYearDesc(Long userId);
 
    // Verify ownership before update/delete
    //  WHERE eduId = ? AND employee.userId = ?
    Optional<EmployeeEducation> findByEduIdAndEmployeeUserId( Integer eduId, Long userId);
 
    // Check if employee has any education records
    boolean existsByEmployeeUserId(Long userId);
 
    // Count education records for employee
    long countByEmployeeUserId(Long userId);
}