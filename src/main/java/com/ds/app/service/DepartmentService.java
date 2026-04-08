package com.ds.app.service;

import com.ds.app.dto.request.DepartmentRequestDTO;
import com.ds.app.dto.response.DepartmentResponseDTO;
import com.ds.app.entity.Department;
import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO create(DepartmentRequestDTO req);

    DepartmentResponseDTO update(Long id, DepartmentRequestDTO req);

    List<DepartmentResponseDTO> getAll();

    DepartmentResponseDTO getById(Long id);

    List<DepartmentResponseDTO> getByCompany(Long companyId);

    DepartmentResponseDTO updateStatus(Long id, String status);

    Department findOrThrow(Long id);
}

