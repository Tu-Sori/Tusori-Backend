package com.example.tusori_backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class JwtAuthenticationProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-time}")
    private long access_token_time;

    @Value("${jwt.refresh-token-time}")
    private long refresh_token_time;


    // Access Token 생성
    public String createAccessToken(Long userId, String name) {
        return createToken(userId, name, "Access", access_token_time);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId, String name) {
        return createToken(userId, name, "Refresh", refresh_token_time);
    }

    public String createToken(Long userId, String name,
                              String type, Long tokenValidTime) {
        return Jwts.builder()
                .setHeaderParam("type", type) // Header 구성
                .setClaims(createClaims(userId, name)) // Payload - Claims 구성
                .setSubject(userId.toString()) // Payload - Subject 구성
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .compact();
    }

    public static Claims createClaims(Long userId, String name) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("name", name);
        return claims;
    }

    public Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = request.getHeader("Authorization").split(" ")[1].trim();
        String id = getId(accessToken);
        return Long.parseLong(id);
    }

    public String getId(String accessToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}
