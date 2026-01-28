package com.mentortheyoung.moneyflow.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "income")
public class Income {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Setter
    @Column(name = "income")
    private double income;

    @Getter
    @Setter
    @Column(name = "target_saved_money")
    private double targetSavedMoney;

//    @Getter
//    @Setter
//    @Column(name = "saved_money")
//    private double savedMoney;

    public Income(User user, double income, double targetSavedMoney) {
        this.user = user;
        this.income = income;
        this.targetSavedMoney = targetSavedMoney;
    };
}
