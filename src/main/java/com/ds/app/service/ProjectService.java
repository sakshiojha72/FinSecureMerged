package com.ds.app.service;

import com.ds.app.dto.request.ProjectRequestDTO;
import com.ds.app.dto.response.ProjectResponseDTO;
import com.ds.app.entity.Project;
import java.util.List;

public interface ProjectService {

    ProjectResponseDTO create(ProjectRequestDTO req);

    ProjectResponseDTO update(Long id, ProjectRequestDTO req);

    List<ProjectResponseDTO> getAll();

    ProjectResponseDTO getById(Long id);

    List<ProjectResponseDTO> getByCompany(Long companyId);

    ProjectResponseDTO updateStatus(Long id, String status);

    Project findOrThrow(Long id);
}
