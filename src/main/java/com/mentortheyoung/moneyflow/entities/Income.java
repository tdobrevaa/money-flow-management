package com.mentortheyoung.moneyflow.entities;

import com.mentortheyoung.moneyflow.enums.IncomeCategories;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "income")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "income")
    private double income;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_category")
    private IncomeCategories incomeCategories;

    @Column(name = "target_saved_money")
    private double targetSavedMoney;

//    @Column(name = "saved_money")
//    private double savedMoney;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Income(User user, double income, double targetSavedMoney) {
        this.user = user;
        this.income = income;
        this.targetSavedMoney = targetSavedMoney;
    };
}
