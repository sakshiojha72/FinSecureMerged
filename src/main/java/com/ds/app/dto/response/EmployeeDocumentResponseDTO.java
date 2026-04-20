package com.ds.app.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDocumentResponseDTO {

    private Integer documentId;
    private Long userId;
    private String documentType;
    private String documentNumber;  // masked when HR views
    private String documentUrl;
    private LocalDate expiryDate;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
}