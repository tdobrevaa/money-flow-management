package com.mentortheyoung.moneyflow.mappers;

import com.mentortheyoung.moneyflow.dto.ExpensesRequestDTO;
import com.mentortheyoung.moneyflow.dto.ExpensesResponseDTO;
import com.mentortheyoung.moneyflow.entities.Expenses;

public class ExpensesMapper {
    public static Expenses toEntity(ExpensesRequestDTO dto) {
        Expenses expenses = new Expenses();
        expenses.setAmount(dto.getAmount());
        expenses.setExpensesCategories(dto.getExpensesCategories());
        return expenses;
    }

    public static ExpensesResponseDTO toDto(Expenses expenses) {
        ExpensesResponseDTO dto = new ExpensesResponseDTO();
        dto.setId(expenses.getId());
        dto.setAmount(expenses.getAmount());
        dto.setExpensesCategories(expenses.getExpensesCategories());
        dto.setDate(expenses.getDate());
        return dto;
    }
}
