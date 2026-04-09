package com.ds.app.service;

import com.ds.app.dto.request.AppraisalRequestDTO;
import com.ds.app.dto.response.AppraisalResponseDTO;
import java.util.Map;

public interface AppraisalService {

    AppraisalResponseDTO initiate(AppraisalRequestDTO req, Long hrUserId);

    Map<String, Object> getHistory(Long employeeUserId, int page, int size);
}
