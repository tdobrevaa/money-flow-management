package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.dto.MonthlyDashboardDTO;
import com.mentortheyoung.moneyflow.entities.UserPrincipal;
import com.mentortheyoung.moneyflow.services.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public MonthlyDashboardDTO getDashboard(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam int month, @RequestParam int year) {
        return dashboardService.getMonthlyDashboard(userPrincipal.getUser(), month, year);
    }
}