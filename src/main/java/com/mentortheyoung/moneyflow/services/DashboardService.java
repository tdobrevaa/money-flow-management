package com.mentortheyoung.moneyflow.services;

import com.mentortheyoung.moneyflow.dto.CategoryTotalDTO;
import com.mentortheyoung.moneyflow.dto.MonthlyDashboardDTO;
import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.repositories.ExpensesRepository;
import com.mentortheyoung.moneyflow.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    private final ExpensesRepository expensesRepository;
    private final IncomeRepository incomeRepository;

    public DashboardService(ExpensesRepository expensesRepository, IncomeRepository incomeRepository) {
        this.expensesRepository = expensesRepository;
        this.incomeRepository = incomeRepository;
    }

    public MonthlyDashboardDTO getMonthlyDashboard(User user, int month, int year) {
        double totalSpent = expensesRepository.getTotalSpent(user, month, year);
        double totalSaved = incomeRepository.getTotalSaved(user, month, year);

        List<CategoryTotalDTO> topCategories = expensesRepository.getTopCategories(user, month, year)
                .stream()
                .limit(3)
                .toList();

        return new MonthlyDashboardDTO(totalSpent, totalSaved, topCategories);
    }
}