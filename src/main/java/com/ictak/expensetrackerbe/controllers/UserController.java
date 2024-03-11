package com.ictak.expensetrackerbe.controllers;

import com.ictak.expensetrackerbe.models.User;
import com.ictak.expensetrackerbe.repository.UserRepository;
import com.ictak.expensetrackerbe.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // private final String secretKey = "ictak1234567890ictakacademyqshvasjkgkjagaskjgaskjkjkjkjgkjagakjs"; // Replace with a secure secret key
    private final SecretKey secretKey = JwtUtils.getHs512SecretKey();

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginCheckUser(@RequestBody User users) {
        User userData = userRepository.userlogin(users.getUserName(), users.getPassword());
        Map<String, Object> response = new HashMap<>();

        if (userData != null) {
            // Token Generated
            String token = Jwts.builder()
                    .setSubject(String.valueOf(userData.getUserName()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 1 hour
                    .signWith(secretKey) // Generate a secure key
                    .compact();
            System.out.println("Generated Token: " + token);
            response.put("status", "success");
            response.put("userId", userData.getId());
            response.put("isAdmin", userData.isAdmin());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            return ResponseEntity.ok(response);
        }
    }
}
