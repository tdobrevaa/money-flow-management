package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.UserPrincipal;
import com.mentortheyoung.moneyflow.services.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("/user/income")
    public ResponseEntity<Income> addIncome(@RequestBody Income income,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Income addedIncome = incomeService.saveIncome(income, userPrincipal.getUser());
        return new ResponseEntity<>(addedIncome, HttpStatus.CREATED);
    }

    @GetMapping("/user/income")
    public ResponseEntity<Income> showIncome(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Income income = incomeService.readIncome(userPrincipal.getUser());

        return new ResponseEntity<>(income, HttpStatus.OK);
    }

    @PutMapping("/user/income")
    public ResponseEntity<Income> updateIncome(@RequestBody Income income,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Income updatedIncome = incomeService.updateIncome(income, userPrincipal.getUser());
        return new ResponseEntity<>(updatedIncome, HttpStatus.OK);
    }

    @DeleteMapping("/user/income")
    public ResponseEntity<Income> deleteIncome(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        incomeService.deleteIncome(userPrincipal.getUser());

        return ResponseEntity.noContent().build();
    }
}
