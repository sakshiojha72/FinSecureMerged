package com.ds.app.service;
 
import java.util.List;
 
import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;
 
public interface EmployeeEducationService {
 
   
    EmployeeEducationResponseDTO addEducation(EmployeeEducationRequestDTO dto, Long userId) throws Exception;
 
    List<EmployeeEducationResponseDTO> getMyEducation(Long userId);
 
    EmployeeEducationResponseDTO updateEducation(Integer eduId, EmployeeEducationRequestDTO dto,Long userId);
 
    void deleteEducation(Integer eduId, Long userId);
 
    List<EmployeeEducationResponseDTO> getEducationByUserId(Long userId);
    
    
}//endclass