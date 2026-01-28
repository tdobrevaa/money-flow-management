package com.mentortheyoung.moneyflow.services;

import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.repositories.ExpensesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesService {
    private final ExpensesRepository expensesRepository;

    public ExpensesService(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }

    public Expenses saveExpense(Expenses expense, User user) {
        if(expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense must be greater than 0!");
        }
        expense.setUser(user);
        return expensesRepository.save(expense);
    }

    public List<Expenses> getAllExpenses(User user) {
        return expensesRepository.findAllByUser(user);
    }

    public Expenses updateExpense(Integer expenseId, Expenses newExpense, User user) {
        Expenses expense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found."));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this expense");
        }

        expense.setAmount(newExpense.getAmount());
        return expensesRepository.save(expense);
    }

    public void deleteExpense(Integer expenseId, User user) {
        Expenses expense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this expense");
        }

        expensesRepository.delete(expense);
    }
}