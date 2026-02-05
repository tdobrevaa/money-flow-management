package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.dto.IncomeRequestDTO;
import com.mentortheyoung.moneyflow.dto.IncomeResponseDTO;
import com.mentortheyoung.moneyflow.entities.Income;
import com.mentortheyoung.moneyflow.entities.UserPrincipal;
import com.mentortheyoung.moneyflow.mappers.IncomeMapper;
import com.mentortheyoung.moneyflow.services.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/income")
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> addIncome(@RequestBody IncomeRequestDTO dto,
                                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Income income = IncomeMapper.toEntity(dto);
        Income addedIncome = incomeService.saveIncome(income, userPrincipal.getUser());
        return new ResponseEntity<>(IncomeMapper.toDto(addedIncome), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> showIncome(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                incomeService.getAllIncome(userPrincipal.getUser())
                        .stream()
                        .map(IncomeMapper::toDto)
                        .toList()
        );
    }

    @PutMapping("/{incomeId}")
    public ResponseEntity<IncomeResponseDTO> updateIncome(@PathVariable Integer incomeId,
                                               @RequestBody IncomeRequestDTO dto,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Income income = IncomeMapper.toEntity(dto);
        Income updatedIncome = incomeService.updateIncome(incomeId, income, userPrincipal.getUser());
        return new ResponseEntity<>(IncomeMapper.toDto(updatedIncome), HttpStatus.OK);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Income> deleteIncome(@PathVariable Integer incomeId,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        incomeService.deleteIncome(incomeId, userPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
