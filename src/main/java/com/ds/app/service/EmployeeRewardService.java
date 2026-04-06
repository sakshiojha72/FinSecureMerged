package com.ds.app.service;

import java.util.List;

import com.ds.app.dto.request.EmployeeRewardRequestDTO;
import com.ds.app.dto.response.EmployeeRewardResponseDTO;

import jakarta.validation.Valid;

public interface EmployeeRewardService {
	
	//HR gives reward to employee
	EmployeeRewardResponseDTO giveReward(Long userId, EmployeeRewardRequestDTO dto, String hrUsername) throws Exception;
	
	//HR updates a reward
	EmployeeRewardResponseDTO updateReward(Integer rewardId, EmployeeRewardRequestDTO dto);
	
	//HR deletes a reward
	void deleteReward(Integer rewardId);
	
	//Employee views own rewards
	List<EmployeeRewardResponseDTO> getMyRewards(Long userId);
	
	//HR view any employee rewards
	List<EmployeeRewardResponseDTO> getRewardsByUserId(Long userId);
	
	//HR views top rewarded employees
	List<Object[]> getTopRewardedEmployees();
	
}
