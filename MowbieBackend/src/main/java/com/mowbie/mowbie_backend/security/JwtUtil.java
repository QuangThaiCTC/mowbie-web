package com.mowbie.mowbie_backend.security;

import io.jsonwebtoken.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Tạo JWT
    public String generateToken(String email, long expiryDuration) {
        return Jwts.builder()
                .setSubject(String.valueOf(email))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryDuration))
                .signWith(SignatureAlgorithm.HS256, SecurityInfo.SECRET_KEY)
                .compact();
    }

    // Giải mã JWT
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SecurityInfo.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy Email từ JWT
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Kiểm tra token có hợp lệ không
    public boolean isValidToken(String token) {
        try {
            return extractClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
