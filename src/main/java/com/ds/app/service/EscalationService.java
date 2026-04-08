package com.ds.app.service;

import com.ds.app.dto.request.EscalationRequestDTO;
import com.ds.app.dto.response.EscalationResponseDTO;
import java.util.Map;

public interface EscalationService {

    EscalationResponseDTO raise(EscalationRequestDTO req, Long raisedByUserId, String role);

    Map<String, Object> getAll(int page, int size);

    Map<String, Object> getForEmployee(Long targetUserId, String role, int page, int size);

    EscalationResponseDTO updateStatus(Long escalationId, String newStatus);
}

