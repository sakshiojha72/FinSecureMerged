package com.ds.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String bankName;
	
	@NotBlank
	private String accountHolderName;
	
	@NotBlank
	@Pattern(regexp = "^[0-9]{9,18}$")
	private String accountNumber;
	
	@NotBlank
	@Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$")
	private String ifscCode;
	
	private String branchName;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	private Boolean approvedBank =  false;
	private Boolean alertRaised =  false;
	
	@OneToOne
	@JoinColumn(name = "employee_id", nullable = false, unique = true)
	private Employee employee;
}
