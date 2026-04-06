package com.ds.app.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRequestDTO {
	
	@NotNull(message = "Deaprtment Id is Required")
	private Long departmentId;
	
	@NotNull(message = "Company Id is Required")
	private Long companyId;
	
	@NotNull(message = "Project Id is Required")
	private Long projectId;

}
