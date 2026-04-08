package com.ds.app.service;

import java.util.List;

import com.ds.app.dto.request.EmployeeRewardRequestDTO;
import com.ds.app.dto.response.EmployeeRewardResponseDTO;

public interface EmployeeRewardService {
	
	EmployeeRewardResponseDTO giveReward(Long userId, EmployeeRewardRequestDTO dto, String hrUsername) throws Exception;
	
	EmployeeRewardResponseDTO updateReward(Integer rewardId, EmployeeRewardRequestDTO dto);

	void deleteReward(Integer rewardId);
	
	List<EmployeeRewardResponseDTO> getMyRewards(Long userId);
	
	List<EmployeeRewardResponseDTO> getRewardsByUserId(Long userId);
	
	List<Object[]> getTopRewardedEmployees();
	
}//endclass
