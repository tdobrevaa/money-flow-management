package com.mentortheyoung.moneyflow.dto;

import com.mentortheyoung.moneyflow.enums.ExpensesCategories;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryTotalDTO {
    private ExpensesCategories category;
    private double total;
}
