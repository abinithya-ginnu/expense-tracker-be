package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.CategoryEntity;
import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.dbmodels.PaymentTypeEntity;
import com.ictak.expensetrackerbe.repository.CategoryRepository;
import com.ictak.expensetrackerbe.repository.ExpenseRepository;
import com.ictak.expensetrackerbe.repository.PaymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExpenseController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> response = new HashMap<>();
        try{
            List<CategoryEntity> result = categoryRepository.getCategories();
            if (result != null) {
                response.put("status", "success");
                response.put("code", 200);
                response.put("categories", result);
            } else {
                response.put("code", 404);
                response.put("categories", new ArrayList<>());
            }
        } catch (Exception e){
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/paymenttypes")
    public ResponseEntity<Map<String, Object>> getPaymentTypes() {
        Map<String, Object> response = new HashMap<>();
        try{
            List<PaymentTypeEntity> result = paymentTypeRepository.getPaymentTypes();
            if (result != null) {
                response.put("status", "success");
                response.put("code", 200);
                response.put("paymentTypes", result);
            } else {
                response.put("code", 404);
                response.put("paymentTypes", new ArrayList<>());
            }
        } catch (Exception e){
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @PostMapping("/addexpense")
    public ResponseEntity<Map<String, Object>> createExpense (@RequestHeader(name = "Authorization") String token,
                                                              @RequestBody ExpenseEntity expense){
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated())
            {
                expense.setCreatedDate(LocalDateTime.now());
                expense.setModifiedDate(LocalDateTime.now());
                ExpenseEntity result = expenseRepository.save(expense);
                response.put("status", "success");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            else {
                // User is not authenticated (token validation failed)
                response.put("status", "error");
                response.put("message", "Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e){
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}





