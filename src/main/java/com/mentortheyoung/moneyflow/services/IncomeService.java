package com.mentortheyoung.moneyflow.services;

import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.enums.IncomeCategories;
import com.mentortheyoung.moneyflow.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public Income saveIncome(Income income, User user) {
        income.setUser(user);
        if (income.getIncome() <= 0) {
            throw new IllegalArgumentException("Income must be greater than 0!");
        }

        if (income.getTargetSavedMoney() < 0 || income.getTargetSavedMoney() > income.getIncome()) {
            throw new IllegalArgumentException("Target saved money must be between 0 and total income!");
        }

        if (income.getIncomeCategories() == IncomeCategories.SALARY) {
            if (income.getStartDate() == null) {
                throw new IllegalArgumentException("Start date is required!");
            }
            income.setEndDate(income.getStartDate().plusMonths(1));
        }

        //savedMoney

        return incomeRepository.save(income);
    }

    public List<Income> getAllIncome(User user) {
        return incomeRepository.findAllByUser(user);
    }

    public Income updateIncome(Integer incomeId,Income newIncome, User user) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found."));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this income!");
        }

        income.setIncome(newIncome.getIncome());
        income.setTargetSavedMoney(newIncome.getTargetSavedMoney());
        income.setIncomeCategories(newIncome.getIncomeCategories());

        income.setStartDate(newIncome.getStartDate());
        if (income.getIncomeCategories() == IncomeCategories.SALARY) {
            income.setEndDate(newIncome.getStartDate().plusMonths(1));
        }
        else {
            income.setEndDate(null);
        }

        return incomeRepository.save(income);
    }

    public void deleteIncome(Integer incomeId, User user) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found."));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this income!");
        }

        incomeRepository.delete(income);
    }
}