package com.mentortheyoung.moneyflow.dto;

import com.mentortheyoung.moneyflow.enums.IncomeCategories;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IncomeResponseDTO {
    private Integer id;
    private double income;
    private double targetSavedMoney;
    private IncomeCategories incomeCategories;
    private LocalDate startDate;
    private LocalDate endDate;
}
