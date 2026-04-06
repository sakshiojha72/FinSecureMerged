package com.ds.app.dto.request;

import java.time.LocalDate;

import com.ds.app.enums.RewardCategory;
import com.ds.app.enums.RewardType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRewardRequestDTO {
	
	
	private RewardType rewardType;
	
	@NotBlank(message = "Title is required")
	@Size(max = 200)
	private String title;
	
	@Size(max = 500)
	private String description;
	
	@NotNull(message = "Reward Date is required")
	private LocalDate rewardDate;
	
	private RewardCategory rewardCategory;
	
	private String givenBy;

	
}//end class
