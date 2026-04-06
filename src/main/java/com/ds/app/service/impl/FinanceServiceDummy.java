// Location: src/main/java/com/ds/app/service/impl/FinanceServiceDummy.java

package com.ds.app.service.impl;

import org.springframework.stereotype.Service;

import com.ds.app.dto.response.BankDetailResponseDTO;
import com.ds.app.dto.response.InvestmentDetailResponseDTO;
import com.ds.app.enums.InvestmentType;
import com.ds.app.service.FinanceService;


@Service
public class FinanceServiceDummy implements FinanceService {

    @Override
    public BankDetailResponseDTO getBankByUserId(Long userId) {
        BankDetailResponseDTO bank = new BankDetailResponseDTO();
        bank.setBankDetailId(userId);
        bank.setUserId(userId);
        bank.setAccountHolderName("Employee " + userId);
        bank.setBankName("ICICI Bank");
        bank.setAccountNumber("9876543210");
        bank.setIfscCode("ICIC0000125");
        bank.setBranchName("Mumbai Branch");
        return bank;
    }

    @Override
    public InvestmentDetailResponseDTO getInvestmentByUserId(Long userId) {
        InvestmentDetailResponseDTO investment = new InvestmentDetailResponseDTO();
        investment.setInvestmentId(userId);
        investment.setUserId(userId);
        investment.setInvestmentType(InvestmentType.MUTUAL_FUND);
        investment.setDeclaredToSystem(true);
        investment.setDirectInvestment(false);
        investment.setWhitelistedFund(true);
        investment.setComplianceStatus("APPROVED");
        investment.setComplianceRemark("All investments are compliant");
        return investment;
    }
}
