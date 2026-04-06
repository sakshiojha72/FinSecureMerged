package com.ds.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailResponseDTO {
	
	 private Long bankDetailId;
	 private Long userId;
	    private String accountHolderName;
	    private String bankName;
	    private String accountNumber;
	    private String ifscCode;
	    private String branchName;

}
