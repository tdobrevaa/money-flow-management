package com.mentortheyoung.moneyflow.services;

import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

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

        return incomeRepository.save(income);
    }
}
