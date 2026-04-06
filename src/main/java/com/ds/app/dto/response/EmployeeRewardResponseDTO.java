package com.ds.app.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ds.app.enums.RewardCategory;
import com.ds.app.enums.RewardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRewardResponseDTO {
	
	private Integer rewardId;
	private Long userId;
	private RewardType rewardType;
	private String title;
	private String description;
	private LocalDate rewardDate;
	private String givenBy;
	private RewardCategory rewardCategory;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
