package com.mentortheyoung.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mentortheyoung.moneyflow.enums.ExpensesCategories;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpensesResponseDTO {
    private Integer id;
    private double amount;
    private ExpensesCategories expensesCategories;
    private LocalDate date;
}
