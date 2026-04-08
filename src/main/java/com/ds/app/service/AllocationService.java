package com.ds.app.service;

import com.ds.app.dto.request.AllocationRequestDTO;
import com.ds.app.dto.request.DeallocationRequestDTO;
import com.ds.app.entity.AllocationHistory;
import java.util.List;

public interface AllocationService {

	String assign(AllocationRequestDTO req, Long performedByUserId);

	String deallocate(DeallocationRequestDTO req, Long performedByUserId);

	List<AllocationHistory> getHistory(Long employeeId);
}
