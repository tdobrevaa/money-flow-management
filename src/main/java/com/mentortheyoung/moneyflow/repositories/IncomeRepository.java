package com.mentortheyoung.moneyflow.repositories;

import com.mentortheyoung.moneyflow.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
