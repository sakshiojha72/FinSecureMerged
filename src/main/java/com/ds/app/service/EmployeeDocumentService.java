package com.ds.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;


public interface EmployeeDocumentService {
 
    EmployeeDocumentResponseDTO uploadDocument(EmployeeDocumentRequestDTO dto, MultipartFile file, Long userId) throws Exception;

    List<EmployeeDocumentResponseDTO> getMyDocuments(Long userId);

    void deleteDocument(Integer documentId, Long userId);

    List<EmployeeDocumentResponseDTO> getDocumentsByUserId(Long userId);

    EmployeeDocumentResponseDTO verifyDocument(Integer documentId);
    
}//end class