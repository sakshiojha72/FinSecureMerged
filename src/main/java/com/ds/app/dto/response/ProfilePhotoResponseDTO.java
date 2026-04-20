package com.ds.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePhotoResponseDTO {
	
	 private String profilePhotoUrl;
	 private String message;

}
