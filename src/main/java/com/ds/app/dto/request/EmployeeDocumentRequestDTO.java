package com.ds.app.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentRequestDTO {

    @NotBlank(message = "Document type is required")
    private String documentType;    // AADHAAR, PAN, PASSPORT, etc.

    @Size(max = 50)
    private String documentNumber;

    private LocalDate expiryDate;
    
}