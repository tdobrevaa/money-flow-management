package com.mentortheyoung.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mentortheyoung.moneyflow.enums.ExpensesCategories;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpensesRequestDTO {
    private double amount;
    private ExpensesCategories expensesCategories;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;
}