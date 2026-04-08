package com.ds.app.service;

import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<String, Long> getSummary();

    Map<String, Long> countGroupByCompany();

    Map<String, Long> countGroupByDepartment();

    Map<String, Long> countGroupByEmployeeType();

    Map<String, Long> countGroupByStatus();

    long countByCompany(Long companyId);

    long countByDepartment(Long deptId);

    Map<String, Map<String, List<String>>> getCompanyPerspectiveReport();
}

