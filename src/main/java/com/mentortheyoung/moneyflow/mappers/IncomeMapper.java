package com.mentortheyoung.moneyflow.mappers;

import com.mentortheyoung.moneyflow.dto.IncomeRequestDTO;
import com.mentortheyoung.moneyflow.dto.IncomeResponseDTO;
import com.mentortheyoung.moneyflow.entities.Income;

public class IncomeMapper {
    public static Income toEntity(IncomeRequestDTO dto) {
        Income income = new Income();
        income.setIncome(dto.getIncome());
        income.setTargetSavedMoney(dto.getTargetSavedMoney());
        income.setIncomeCategories(dto.getIncomeCategories());
        income.setStartDate(dto.getStartDate());
        return income;
    }

    public static IncomeResponseDTO toDto(Income income) {
        IncomeResponseDTO dto = new IncomeResponseDTO();
        dto.setId(income.getId());
        dto.setIncome(income.getIncome());
        dto.setTargetSavedMoney(income.getTargetSavedMoney());
        dto.setIncomeCategories(income.getIncomeCategories());
        dto.setStartDate(income.getStartDate());
        dto.setEndDate(income.getEndDate());
        return dto;
    }
}
