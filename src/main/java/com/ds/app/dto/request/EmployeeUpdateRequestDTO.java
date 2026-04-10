package com.ds.app.dto.request;

import java.time.LocalDate;

import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.SkillStatus;
import com.ds.app.enums.EmploymentType;
import com.ds.app.enums.Gender;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequestDTO {

	
	    @NotBlank(message = "First name is required")
	    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name can contain only alphabets and spaces" )
	    private String firstName;

	    @NotBlank(message = "Last name is required")
	    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
	    @Pattern( regexp = "^[A-Za-z ]+$",
	        message = "Last name can contain only alphabets and spaces"
	    )
	    private String lastName;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Enter a valid email address")
	    @Size(max = 100, message = "Email cannot exceed 100 characters")
	    private String email;

	    @NotBlank(message = "Phone number is required")
	    @Pattern(
	        regexp = "^[0-9]{10}$",
	        message = "Phone number must be exactly 10 digits"
	    )
	    private String phoneNumber;

	    @NotNull(message = "Date of birth is required")
	    @Past(message = "Date of birth must be in the past")
	    private LocalDate dateOfBirth;

	    @NotNull(message = "Gender is required")
	    private Gender gender;

	    

	  

	    @NotBlank(message = "Address line is required")
	    @Size(max = 255, message = "Address line cannot exceed 255 characters")
	    private String addressLine;

	    @NotBlank(message = "City is required")
	    @Size(max = 100, message = "City cannot exceed 100 characters")
	    private String city;

	    @NotBlank(message = "State is required")
	    @Size(max = 100, message = "State cannot exceed 100 characters")
	    private String state;

	    @NotBlank(message = "Country is required")
	    @Size(max = 100, message = "Country cannot exceed 100 characters")
	    private String country;

	    @NotBlank(message = "Pincode is required")
	    @Pattern(
	        regexp = "^[0-9]{6}$",
	        message = "Pincode must be exactly 6 digits"
	    )
	    private String pincode;
	    
	    @Size(max = 255, message = "Profile photo URL is too long")
	    @Column(length = 255)
	    private String profilePhotoUrl;

}//end class
