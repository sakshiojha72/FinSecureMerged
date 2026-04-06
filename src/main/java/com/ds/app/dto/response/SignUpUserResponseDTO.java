package com.ds.app.dto.response;

import com.ds.app.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpUserResponseDTO {
	
	private Long userId;
	private String username;
	private UserRole role;
	//private String employeeCode;
	//private String firstName;
	//private String lastName;
}
