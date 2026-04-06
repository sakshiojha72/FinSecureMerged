package com.ds.app.dto.response;

import com.ds.app.dto.response.BankDetailResponseDTO;
import com.ds.app.dto.response.EmployeeResponseDTO;
import com.ds.app.dto.response.InvestmentDetailResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFinanceViewDTO {
	
	private EmployeeResponseDTO employee;
	private BankDetailResponseDTO bankDetail;
	private InvestmentDetailResponseDTO investmentDetail;

}
