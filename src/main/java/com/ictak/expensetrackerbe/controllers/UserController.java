package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.dbmodels.FamilyEntity;
import com.ictak.expensetrackerbe.dbmodels.UserEntity;
import com.ictak.expensetrackerbe.models.UserModel;
import com.ictak.expensetrackerbe.repository.FamilyRepository;
import com.ictak.expensetrackerbe.repository.UserRepository;
import com.ictak.expensetrackerbe.utils.JwtUtils;
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
                response.put("name", userData.getName());
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
    @GetMapping("/emailexists")
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
    @GetMapping("/codeexists")
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
                familyData.setCreatedDate(LocalDateTime.now());
                familyData.setModifiedDate(LocalDateTime.now());
                FamilyEntity family = familyRepository.save(familyData);
                familyId = family.getId();
            }
            UserEntity userData = new UserEntity();
            userData.setName(user.getName());
            userData.setEmail(user.getEmail());
            userData.setPassword(user.getPassword());
            userData.setFamilyId(familyId);
            userData.setAdmin(isAdmin);
            userData.setCreatedDate(LocalDateTime.now());
            userData.setModifiedDate(LocalDateTime.now());
            UserEntity result = userRepository.save(userData);
            response.put("status", "success");
            response.put("code", 201);
            response.put("id", result.getId());
            response.put("isAdmin", result.isAdmin());
            response.put("name", result.getName());
            response.put("token", JwtUtils.generateToken(result.getEmail()));
        } catch (Exception ex) {
            response.put("status", "error");
            response.put("code", 500);
            response.put("message", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfileDetails (@RequestHeader(name = "Authorization") String token,
                                                                    @RequestParam int userId)
    {
        Map<String, Object> response = new HashMap<>();
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                UserEntity userData = userRepository.getByUserId(userId);
                List<Map<String, Object>> familyDetails = new ArrayList<>();
                if (userData != null) {
                    familyDetails = familyRepository.getFamilyDetails(userData.getFamilyId());
                }
                response.put("status", "success");
                response.put("code" , 200);
                response.put("profile", userData);
                response.put("family", familyDetails);
            } else {
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

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*" )
    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, Object>> updateProfileDetails (@RequestHeader(name = "Authorization") String token,
                                                                  @RequestParam int id,
                                                                  @RequestBody UserModel user)
    {
        Map<String, Object> response = new HashMap<>();
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                userRepository.updateProfile(user.getName(), user.getPassword(), user.getEmail(), id);
                response.put("status", "success");
                response.put("code" , 200);
            } else {
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
