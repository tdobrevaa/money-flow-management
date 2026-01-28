package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.UserPrincipal;
import com.mentortheyoung.moneyflow.services.ExpensesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/expenses")
public class ExpensesController {
    private final ExpensesService expensesService;

    public ExpensesController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @PostMapping
    public ResponseEntity<Expenses> addExpense(@RequestBody Expenses expenses,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Expenses addedExpense = expensesService.saveExpense(expenses, userPrincipal.getUser());
        return new ResponseEntity<>(addedExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Expenses>> getAllExpenses(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                expensesService.getAllExpenses(userPrincipal.getUser())
        );
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expenses> updateExpense(@PathVariable Integer expenseId,
                                                  @RequestBody Expenses expenses,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Expenses updatedExpense = expensesService.updateExpense(expenseId, expenses, userPrincipal.getUser());
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Expenses> deleteExpense(@PathVariable Integer expenseId,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expensesService.deleteExpense(expenseId,userPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
