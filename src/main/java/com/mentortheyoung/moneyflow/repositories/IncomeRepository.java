package com.mentortheyoung.moneyflow.repositories;

import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findAllByUser(User user);

    @Query("""
        SELECT SUM(income.targetSavedMoney)
        FROM Income income
        WHERE income.user = :user
        AND MONTH(income.startDate) = :month
        AND YEAR(income.startDate) = :year
        """)
    Double getTotalSaved(User user, int month, int year);
}