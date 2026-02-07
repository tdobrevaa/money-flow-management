package com.mentortheyoung.moneyflow.repositories;

import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    List<Expenses> findAllByUser(User user);
}