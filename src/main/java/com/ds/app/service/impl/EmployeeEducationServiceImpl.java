package com.ds.app.service.impl;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.app.dto.request.EmployeeEducationRequestDTO;
import com.ds.app.dto.response.EmployeeEducationResponseDTO;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeEducation;
import com.ds.app.exception.EmployeeNotFoundException;
import com.ds.app.repository.IEmployeeEducationRepository;
import com.ds.app.repository.IEmployeeRepository;
import com.ds.app.service.EmployeeEducationService;
 
@Service
@Transactional
public class EmployeeEducationServiceImpl implements EmployeeEducationService {
 
    private static final Logger logger = LoggerFactory.getLogger(EmployeeEducationServiceImpl.class);
 
		
		@Autowired
		IEmployeeEducationRepository educationRepo;
		@Autowired
		IEmployeeRepository iEmployeeRepo;
 
  

    @Override
    public EmployeeEducationResponseDTO addEducation( EmployeeEducationRequestDTO dto, Long userId) throws Exception {
 
        logger.info("Adding education for userId: {}", userId);
 
        // Fetch Employee object — needed for @ManyToOne
        Employee employee = iEmployeeRepo.findByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFoundException(userId));
 
        EmployeeEducation education = new EmployeeEducation();
        education.setEmployee(employee);          // set Employee object
        education.setDegree(dto.getDegree());
        education.setInstitution(dto.getInstitution());
        education.setFieldOfStudy(dto.getFieldOfStudy());
        education.setPassingYear(dto.getPassingYear());
        education.setPercentage(dto.getPercentage());
        education.setGrade(dto.getGrade());
        education.setLocation(dto.getLocation());
 
        EmployeeEducation saved = educationRepo.save(education);
        logger.info("Education added for userId: {}", userId);
        return mapToResponse(saved);
    }
 
    // ════════════════════════════════════════════════════════════════
    // GET MY EDUCATION — Employee view
    //
    // WHAT IT DOES:
    //   Returns all education records for logged in employee.
    //   Sorted by passingYear DESC — most recent first.
    //   userId comes from JWT token in controller.
    // ════════════════════════════════════════════════════════════════
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeEducationResponseDTO> getMyEducation(Long userId) {
        logger.info("Fetching education for userId: {}", userId);
 
        return educationRepo
                .findByEmployeeUserIdOrderByPassingYearDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
 
    // ════════════════════════════════════════════════════════════════
    // UPDATE EDUCATION
    //
    // WHAT IT DOES:
    //   Employee updates one of their own education records.
    //   Verifies the record belongs to logged in employee
    //   using findByEduIdAndEmployeeUserId —
    //   employee cannot edit another employee's record.
    //   Partial update — only non-null fields are updated.
    // ════════════════════════════════════════════════════════════════
    @Override
    public EmployeeEducationResponseDTO updateEducation(Integer eduId, EmployeeEducationRequestDTO dto, Long userId) {
 
        logger.info("Updating eduId: {} for userId: {}", eduId, userId);
 
        // Verify ownership
        EmployeeEducation education = educationRepo
                .findByEduIdAndEmployeeUserId(eduId, userId)
                .orElseThrow(() -> new RuntimeException( "Education record not found or " + "does not belong to you: " + eduId));
 
        // Partial update — only apply non-null fields
        if (dto.getDegree() != null && !dto.getDegree().isBlank())
            education.setDegree(dto.getDegree());
        if (dto.getInstitution() != null && !dto.getInstitution().isBlank())
        	education.setInstitution(dto.getInstitution());
        if (dto.getFieldOfStudy() != null)
            education.setFieldOfStudy(dto.getFieldOfStudy());
        if (dto.getPassingYear() != null)
            education.setPassingYear(dto.getPassingYear());
        if (dto.getPercentage() != null)
            education.setPercentage(dto.getPercentage());
        if (dto.getGrade() != null)
            education.setGrade(dto.getGrade());
        if (dto.getLocation() != null)
            education.setLocation(dto.getLocation());
 
        EmployeeEducation updated = educationRepo.save(education);
        logger.info("Education updated: eduId: {}", eduId);
        return mapToResponse(updated);
    }
 
    // ════════════════════════════════════════════════════════════════
    // DELETE EDUCATION
    //
    // WHAT IT DOES:
    //   Employee permanently deletes their own education record.
    //   Verifies ownership before deleting.
    //   Hard delete — record permanently removed.
    // ════════════════════════════════════════════════════════════════
    @Override
    public void deleteEducation(Integer eduId, Long userId) {
        logger.warn("Deleting eduId: {} for userId: {}",eduId, userId);
 
        // Verify ownership
        EmployeeEducation education = educationRepo
                .findByEduIdAndEmployeeUserId(eduId, userId)
                .orElseThrow(() -> new RuntimeException("Education record not found or " + "does not belong to you: " + eduId));
 
        educationRepo.delete(education);
        logger.info("Education deleted: eduId: {}", eduId);
    }
 
    // ════════════════════════════════════════════════════════════════
    // GET EDUCATION BY USERID — HR view
    //
    // WHAT IT DOES:
    //   HR views any employee's education records by userId.
    //   HR gets userId from search results first.
    //   Sorted by passingYear DESC — most recent first.
    // ════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public List<EmployeeEducationResponseDTO> getEducationByUserId( Long userId) {
        logger.info("HR fetching education for userId: {}", userId);
 
        return educationRepo
                .findByEmployeeUserIdOrderByPassingYearDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
 
    // ── Private mapper ─────────────────────────────────────────────
    private EmployeeEducationResponseDTO mapToResponse( EmployeeEducation e) {
    	
        return EmployeeEducationResponseDTO.builder()
                .eduId(e.getEduId())
                .userId(e.getEmployee().getUserId())
                .degree(e.getDegree())
                .institution(e.getInstitution())
                .fieldOfStudy(e.getFieldOfStudy())
                .passingYear(e.getPassingYear())
                .percentage(e.getPercentage())
                .grade(e.getGrade())
                .location(e.getLocation())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
