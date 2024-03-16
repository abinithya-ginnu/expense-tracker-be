package com.ictak.expensetrackerbe.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "ictak1234567890ictakacademyqshvasjkgkjagaskjgaskjkjkjkjgkjagakjs";


    public static SecretKey getHs512SecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static String generateToken(String data) {
        return Jwts.builder()
                .setSubject(String.valueOf(data))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 1 hour
                .signWith(getHs512SecretKey()) // Generate a secure key
                .compact();
    }

}