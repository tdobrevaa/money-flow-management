package com.mentortheyoung.moneyflow.repositories;

import com.mentortheyoung.moneyflow.dto.CategoryTotalDTO;
import com.mentortheyoung.moneyflow.dto.MonthlyDashboardDTO;
import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    List<Expenses> findAllByUser(User user);

    @Query("""
        SELECT SUM(expenses.amount)
        FROM Expenses expenses
        WHERE expenses.user = :user
        AND MONTH(expenses.date) = :month
        AND YEAR(expenses.date) = :year
        """)

    Double getTotalSpent(User user, int month, int year);

    @Query("""
        SELECT new com.mentortheyoung.moneyflow.dto.CategoryTotalDTO(expenses.expensesCategories, SUM (expenses.amount))
        FROM Expenses expenses
        WHERE expenses.user = :user
        AND MONTH(expenses.date) = :month
        AND YEAR(expenses.date) = :year
        GROUP BY expenses.expensesCategories
        ORDER BY SUM(expenses.amount) DESC
        """)
    List<CategoryTotalDTO> getTopCategories(User user, int month, int year);
}