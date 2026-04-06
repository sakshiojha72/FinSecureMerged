

package com.ds.app.service;

import com.ds.app.dto.response.BankDetailResponseDTO;
import com.ds.app.dto.response.InvestmentDetailResponseDTO;

public interface FinanceService {

    BankDetailResponseDTO getBankByUserId(Long userId);

    InvestmentDetailResponseDTO getInvestmentByUserId(Long userId);
}
