package com.ds.app.service;

import com.ds.app.dto.request.EmployeeRequestDTO;
import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.entity.Employee;
import java.util.Map;

public interface EmployeeService {

    EmployeeResponseDTO getById(Long userId);

    Map<String, Object> getAllEmployees(int page, int size);

    EmployeeResponseDTO updateEmployee(Long userId, EmployeeRequestDTO req);

    String softDelete(Long userId);

    Map<String, Object> getByCompany(Long companyId, int page, int size);

    Map<String, Object> getByDepartment(Long deptId, int page, int size);

    Map<String, Object> getByProject(Long projectId, int page, int size);

    Map<String, Object> getEscalated(int page, int size);

    Map<String, Object> getUnassigned(int page, int size);

    long countByCompany(Long companyId);

    long countByDepartment(Long deptId);

    Employee findOrThrow(Long userId);
}
