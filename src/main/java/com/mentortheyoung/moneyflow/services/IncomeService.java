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

        //savedMoney

        return incomeRepository.save(income);
    }

    public Income readIncome(User user) {
        return incomeRepository.findByUser(user);
    }

    public Income updateIncome(Income newIncome, User user) {
        Income income = incomeRepository.findByUser(user);
        income.setIncome(newIncome.getIncome());
        income.setTargetSavedMoney(newIncome.getTargetSavedMoney());

        return incomeRepository.save(income);
    }

    public void deleteIncome(User user) {
        Income income = incomeRepository.findByUser(user);
        incomeRepository.delete(income);
    }
}
