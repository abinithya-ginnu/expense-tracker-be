package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import com.ictak.expensetrackerbe.repository.IncomeRepository;
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
public class IncomeController {
    @Autowired
    private IncomeRepository incomeRepository;

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @PostMapping("/addincome")
    public ResponseEntity<Map<String, Object>> createIncome (@RequestHeader(name = "Authorization") String token,
                                                             @RequestBody IncomeEntity income){

        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated())
            {
                income.setCreatedDate(LocalDateTime.now());
                income.setModifiedDate(LocalDateTime.now());
                IncomeEntity result = incomeRepository.save(income);
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

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @DeleteMapping("/income/delete")
    public ResponseEntity<Map<String, Object>> deleteIncome (@RequestHeader(name = "Authorization") String token,
                                                             @RequestParam int id){

        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated())
            {
                incomeRepository.deleteById(id);
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
