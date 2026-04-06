package com.ds.app.dto.response;

import com.ds.app.enums.InvestmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvestmentDetailResponseDTO {
	
	 private Long investmentId;
	 private Long userId;
	 private InvestmentType investmentType;
	 private Boolean declaredToSystem;
	 private Boolean directInvestment;
	 private Boolean whitelistedFund;
	 private String complianceRemark;
	 private String complianceStatus;

}
