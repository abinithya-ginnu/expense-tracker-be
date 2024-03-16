package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.FamilyEntity;
import com.ictak.expensetrackerbe.dbmodels.UserEntity;
import com.ictak.expensetrackerbe.models.UserModel;
import com.ictak.expensetrackerbe.repository.FamilyRepository;
import com.ictak.expensetrackerbe.repository.UserRepository;
import com.ictak.expensetrackerbe.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FamilyRepository familyRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserModel user) {
        Map<String, Object> response = new HashMap<>();
        try{
            UserEntity userData = userRepository.login(user.getEmail(), user.getPassword());
            if (userData != null) {
                // Token Generated
                response.put("status", "success");
                response.put("code", 200);
                response.put("userId", userData.getId());
                response.put("isAdmin", userData.isAdmin());
                response.put("token", JwtUtils.generateToken(userData.getEmail()));
            } else {
                response.put("code", "404");
            }
        } catch (Exception e){
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/emailExists")
    public ResponseEntity<Map<String, Object>> emailExists (@RequestParam String email){
        Map<String, Object> response = new HashMap<>();
        try{
            UserEntity userData = userRepository.emailExists(email);
            if (userData != null){
                response.put("status", "success");
                response.put("code" , 200);
                response.put("id", userData.getId());
            } else {
                    response.put("code" , 404);
                    response.put("id", "email does not exist");
            }
        } catch (Exception e){
                response.put("status", "error");
                response.put("code", 500);
                response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/codeExists")
    public ResponseEntity<Map<String, Object>> codeExists (@RequestParam String code){
        Map<String, Object> response = new HashMap<>();
        try{
            Integer id = familyRepository.codeExists(code);
            if(id != null && id > 0){
                response.put("status", "success");
                response.put("code", 200);
                response.put("id", id);
            } else {
                response.put("code", 404);
                response.put("id", "code does not exist");
            }
        } catch (Exception e){
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public  ResponseEntity<Map<String, Object>> createUser(@RequestBody UserModel user) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer familyId = familyRepository.codeExists(user.getUniqueCode());
            boolean isAdmin = false;
            if (familyId == null) {
                isAdmin = true;
                FamilyEntity familyData = new FamilyEntity();
                familyData.setCode(user.getUniqueCode());
                FamilyEntity family = familyRepository.save(familyData);
                familyId = family.getId();
            }
            UserEntity userData = new UserEntity();
            userData.setName(user.getName());
            userData.setEmail(user.getEmail());
            userData.setPassword(user.getPassword());
            userData.setFamilyId(familyId);
            userData.setAdmin(isAdmin);
            UserEntity result = userRepository.save(userData);
            response.put("status", "success");
            response.put("code", 201);
            response.put("id", result.getId());
            response.put("isAdmin", result.isAdmin());
            response.put("token", JwtUtils.generateToken(result.getEmail()));
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
