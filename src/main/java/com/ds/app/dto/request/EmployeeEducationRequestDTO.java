package com.ds.app.dto.request;
 
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEducationRequestDTO {
	 
	    @NotBlank(message = "Degree is required")
	    @Size(max = 100, message = "Degree cannot exceed 100 characters")
	    private String degree;
	 
	    @NotBlank(message = "Institution is required")
	    @Size(max = 200, message = "Institution cannot exceed 200 characters")
	    private String institution;
	 
	    @Size(max = 100)
	    private String fieldOfStudy;
	 
	    @Min(value = 1950, message = "Passing year must be after 1950")
	    @Max(value = 2100, message = "Passing year cannot be in far future")
	    private Integer passingYear;
	 
	    @Min(value = 0, message = "Percentage cannot be negative")
	    @Max(value = 100, message = "Percentage cannot exceed 100")
	    private Double percentage;
	 
	    @Size(max = 50)
	    private String grade;
	 
	    @Size(max = 100)
	    private String location;
}