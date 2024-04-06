package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.CategoryEntity;
import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.dbmodels.PaymentTypeEntity;
import com.ictak.expensetrackerbe.dbmodels.UserEntity;
import com.ictak.expensetrackerbe.models.UserModel;
import com.ictak.expensetrackerbe.repository.CategoryRepository;
import com.ictak.expensetrackerbe.repository.ExpenseRepository;
import com.ictak.expensetrackerbe.repository.PaymentTypeRepository;
import com.ictak.expensetrackerbe.utils.OCRUtils;
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
    @Autowired
    private OCRUtils ocrUtils;

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                List<CategoryEntity> result = categoryRepository.getCategories();
                if (result != null) {
                    response.put("status", "success");
                    response.put("code", 200);
                    response.put("categories", result);
                } else {
                    response.put("code", 404);
                    response.put("categories", new ArrayList<>());
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

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @GetMapping("/paymenttypes")
    public ResponseEntity<Map<String, Object>> getPaymentTypes(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                List<PaymentTypeEntity> result = paymentTypeRepository.getPaymentTypes();
                if (result != null) {
                    response.put("status", "success");
                    response.put("code", 200);
                    response.put("paymentTypes", result);
                } else {
                    response.put("code", 404);
                    response.put("paymentTypes", new ArrayList<>());
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

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @PostMapping("/addexpense")
    public ResponseEntity<Map<String, Object>> createExpense (@RequestHeader(name = "Authorization") String token,
                                                              @RequestParam boolean isOCR,
                                                              @RequestBody ExpenseEntity expense){
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated())
            {
                ExpenseEntity data = expense;
                if (isOCR) {
                    data = ocrUtils.extractInvoiceDetails(expense.getTitle(), expense.getUserId());
                }
                data.setCreatedDate(LocalDateTime.now());
                data.setModifiedDate(LocalDateTime.now());
                ExpenseEntity result = expenseRepository.save(data);
                response.put("status", "success");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @DeleteMapping("/expense/delete")
    public ResponseEntity<Map<String, Object>> deleteExpense (@RequestHeader(name = "Authorization") String token,
                                                             @RequestParam int id){

        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated())
            {
                expenseRepository.deleteById(id);
                response.put("status", "success");
                response.put("code", 200);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            else {
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





