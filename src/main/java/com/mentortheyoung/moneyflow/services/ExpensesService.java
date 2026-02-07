package com.mentortheyoung.moneyflow.services;

import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.repositories.ExpensesRepository;
import com.mentortheyoung.moneyflow.repositories.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesService {
    private final ExpensesRepository expensesRepository;
    private final IncomeRepository incomeRepository;

    public ExpensesService(ExpensesRepository expensesRepository, IncomeRepository incomeRepository) {
        this.expensesRepository = expensesRepository;
        this.incomeRepository = incomeRepository;
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

        if (newExpense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense must be greater than 0!");
        }
        expense.setAmount(newExpense.getAmount());
        expense.setExpensesCategories(newExpense.getExpensesCategories());
        expense.setDate(newExpense.getDate());

        return expensesRepository.save(expense);
    }

    public void deleteExpense(Integer expenseId, User user) {
        Expenses expense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to modify this expense!");
        }

        expensesRepository.delete(expense);
    }

    public double calculateBalance(User user) {
        double totalIncome = incomeRepository.findAllByUser(user)
                .stream()
                .mapToDouble(Income::getIncome)
                .sum();

        double totalExpenses = expensesRepository.findAllByUser(user)
                .stream()
                .mapToDouble(Expenses::getAmount)
                .sum();
        return totalIncome - totalExpenses;
    }
}