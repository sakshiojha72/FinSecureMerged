package com.ds.app.service;

import com.ds.app.dto.request.CompanyRequestDTO;
import com.ds.app.dto.response.CompanyResponseDTO;
import com.ds.app.entity.Company;
import java.util.List;

public interface CompanyService {

    CompanyResponseDTO create(CompanyRequestDTO req);

    CompanyResponseDTO update(Long id, CompanyRequestDTO req);

    List<CompanyResponseDTO> getAll();

    CompanyResponseDTO getById(Long id);

    CompanyResponseDTO updateStatus(Long id, String status);

    Company findOrThrow(Long id);
}

