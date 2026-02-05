package com.mentortheyoung.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;
}
