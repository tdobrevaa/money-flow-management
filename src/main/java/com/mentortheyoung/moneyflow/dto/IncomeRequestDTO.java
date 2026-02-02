package com.mentortheyoung.moneyflow.dto;

import com.mentortheyoung.moneyflow.enums.IncomeCategories;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IncomeRequestDTO {
    private double income;
    private double targetSavedMoney;
    private IncomeCategories incomeCategories;
    private LocalDate startDate;
}
