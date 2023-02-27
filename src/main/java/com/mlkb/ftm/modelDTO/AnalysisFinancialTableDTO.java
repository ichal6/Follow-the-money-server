package com.mlkb.ftm.modelDTO;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class AnalysisFinancialTableDTO {
    String name;
    BigDecimal income;
    BigDecimal expense;
    BigDecimal balance;

    @Builder
    public AnalysisFinancialTableDTO(String name, BigDecimal income, BigDecimal expense) {
        this.name = name;
        this.income = income;
        this.expense = expense;
        this.balance = income.subtract(expense);
    }
}
