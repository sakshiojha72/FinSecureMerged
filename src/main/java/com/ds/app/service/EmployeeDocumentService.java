package com.ds.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ds.app.dto.request.*;
import com.ds.app.dto.response.*;


public interface EmployeeDocumentService {

    // Employee uploads a document
    EmployeeDocumentResponseDTO uploadDocument(EmployeeDocumentRequestDTO dto, MultipartFile file, Long userId) throws Exception;

    // Employee views own documents
    List<EmployeeDocumentResponseDTO> getMyDocuments(Long userId);

    // Employee deletes own document
    void deleteDocument(Integer documentId, Long userId);

    // HR views any employee's documents (masked)
    List<EmployeeDocumentResponseDTO> getDocumentsByUserId(Long userId);

    // HR verifies a document
    EmployeeDocumentResponseDTO verifyDocument(Integer documentId);
    
}