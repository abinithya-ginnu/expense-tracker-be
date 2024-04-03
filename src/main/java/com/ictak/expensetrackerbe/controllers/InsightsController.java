package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import com.ictak.expensetrackerbe.repository.ExpenseRepository;
import com.ictak.expensetrackerbe.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InsightsController {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping(value = "/insights", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getInsights(@RequestHeader(name = "Authorization") String token,
                                                                 @RequestParam int userId,
                                                                 @RequestParam String display) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                if (display != null && display.equals("expense")) {
                    List<Map<String, Object>> expenses = expenseRepository.getAllExpensesOfUser(userId);
                    if (expenses != null) {
                        response.put("status", "success");
                        response.put("code", 200);
                        response.put("expense", expenses);
                    } else {
                        response.put("code", 404);
                        response.put("expense", new ArrayList<>());
                    }
                } else if (display != null && display.equals("transactions")) {
                    List<Map<String, Object>> transHistory = expenseRepository.getAllTransactionsOfUser(userId);
                    if (transHistory != null) {
                        response.put("status", "success");
                        response.put("code", 200);
                        response.put("transactions", transHistory);
                    } else {
                        response.put("code", 404);
                        response.put("transactions", new ArrayList<>());
                    }
                } else {
                    List<Map<String, Object>> income = incomeRepository.getAllIncomeOfUser(userId);
                    if (income != null) {
                        response.put("status", "success");
                        response.put("code", 200);
                        response.put("income", income);
                    } else {
                        response.put("code", 404);
                        response.put("income", new ArrayList<>());
                    }
                }
            } else {
                response.put("status", "error");
                response.put("message", "Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
