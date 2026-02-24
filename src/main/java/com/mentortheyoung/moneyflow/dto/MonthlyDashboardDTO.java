package com.mentortheyoung.moneyflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyDashboardDTO {
    private double totalSpent;
    private double totalSaved;
    private List<CategoryTotalDTO> topCategories;
}
