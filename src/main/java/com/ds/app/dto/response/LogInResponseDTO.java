package com.ds.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponseDTO {
	
	private String token;
    private Long userId;
    private String username;
    private String role;
    private String message;

}
