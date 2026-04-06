package com.ds.app.service;
 
import java.util.List;
 
import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
 
public interface EmployeeEducationService {
 
    // Employee adds a new education record
    EmployeeEducationResponseDTO addEducation(EmployeeEducationRequestDTO dto, Long userId) throws Exception;
 
    // Employee views own education records
    List<EmployeeEducationResponseDTO> getMyEducation(Long userId);
 
    // Employee updates own education record
    EmployeeEducationResponseDTO updateEducation(Integer eduId, EmployeeEducationRequestDTO dto,Long userId);
 
    // Employee deletes own education record
    void deleteEducation(Integer eduId, Long userId);
 
    // HR views any employee education records
    List<EmployeeEducationResponseDTO> getEducationByUserId(Long userId);
}