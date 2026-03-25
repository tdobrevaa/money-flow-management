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
        Double totalSpent = expensesRepository.getTotalSpent(user, month, year);
        Double totalSaved = incomeRepository.getTotalSaved(user, month, year);

        List<CategoryTotalDTO> allCategories = expensesRepository.getTopCategories(user, month, year);

        List<CategoryTotalDTO> topCategories = allCategories
                .stream()
                .limit(3)
                .toList();

        return new MonthlyDashboardDTO(
                totalSpent != null ? totalSpent : 0.0,
                totalSaved != null ? totalSaved : 0.0,
                topCategories,
                allCategories
        );
    }
}