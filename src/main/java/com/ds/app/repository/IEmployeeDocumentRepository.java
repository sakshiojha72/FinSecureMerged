package com.ds.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.EmployeeDocument;

@Repository
public interface IEmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Integer> {
			
			// Get all documents for employee
			List<EmployeeDocument> findByEmployeeUserId(Long userId);
			
			// Get specific document — verify ownership
			Optional<EmployeeDocument> findByDocumentIdAndEmployeeUserId(Integer documentId, Long userId);
			
			// Check duplicate document type
			boolean existsByEmployeeUserIdAndDocumentType(Long userId, String documentType);
			
	
	
	
	

//		// Use employee.userId instead of userId
//		@Query("SELECT d FROM EmployeeDocument d " +
//		       "WHERE d.employee.userId = :userId")
//		List<EmployeeDocument> findByUserId(@Param("userId") Long userId);
//		
//		@Query("SELECT d FROM EmployeeDocument d " +
//		       "WHERE d.documentId = :documentId " +
//		       "AND d.employee.userId = :userId")                     // x
//		Optional<EmployeeDocument> findByDocumentIdAndUserId( @Param("documentId") Integer documentId, @Param("userId") Long userId);
//		
//		//
//		@Query("SELECT CASE WHEN COUNT(d) > 0 " +
//		       "THEN true ELSE false END " +
//		       "FROM EmployeeDocument d " +
//		       "WHERE d.employee.userId = :userId " +
//		       "AND LOWER(d.documentType) = LOWER(:documentType)")
//		boolean existsByUserIdAndDocumentType(@Param("userId") Long userId, @Param("documentType") String documentType);
//		


}//end class