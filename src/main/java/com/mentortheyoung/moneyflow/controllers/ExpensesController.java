package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.dto.ExpensesRequestDTO;
import com.mentortheyoung.moneyflow.dto.ExpensesResponseDTO;
import com.mentortheyoung.moneyflow.entities.Expenses;
import com.mentortheyoung.moneyflow.entities.UserPrincipal;
import com.mentortheyoung.moneyflow.mappers.ExpensesMapper;
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
    public ResponseEntity<ExpensesResponseDTO> addExpense(@RequestBody ExpensesRequestDTO dto,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Expenses expenses = ExpensesMapper.toEntity(dto);
        Expenses addedExpense = expensesService.saveExpense(expenses, userPrincipal.getUser());
        return new ResponseEntity<>(ExpensesMapper.toDto(addedExpense), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesResponseDTO>> getAllExpenses(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                expensesService.getAllExpenses(userPrincipal.getUser())
                        .stream()
                        .map(ExpensesMapper::toDto)
                        .toList()
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

    @GetMapping("/balance")
    public double getBalance(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return expensesService.calculateBalance(userPrincipal.getUser());
    }
}
