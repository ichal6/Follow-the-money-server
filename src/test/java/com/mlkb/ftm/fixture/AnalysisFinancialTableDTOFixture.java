package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.AnalysisFinancialTableDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnalysisFinancialTableDTOFixture {
    public static AnalysisFinancialTableDTO bankAnalysisFinancialTableDTO() {
        return AnalysisFinancialTableDTO.builder()
                .expense(new BigDecimal("55.98").setScale(2, RoundingMode.HALF_UP))
                .income(new BigDecimal("1700").setScale(2, RoundingMode.HALF_UP))
                .name("Millenium")
                .build();
    }

    public static AnalysisFinancialTableDTO walletAnalysisFinancialTableDTO() {
        return AnalysisFinancialTableDTO.builder()
                .expense(new BigDecimal("2527.99").setScale(2, RoundingMode.HALF_UP))
                .income(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                .name("Wallet")
                .build();
    }
}
