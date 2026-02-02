package com.mentortheyoung.moneyflow.entities;

import com.mentortheyoung.moneyflow.Categories;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Table (name = "expenses")
public class Expenses {
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
    @Column(name = "amount")
    private double amount;

    @Getter
    @Setter
    @Column(name = "category")
    private List<Categories> category;
}