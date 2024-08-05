package com.example.security;

import com.example.entity.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private String jwtSecret = "VH9CPR8U4EYSJTM0zeOyEalA0p09AVqV/2a7EmpkHyo=";
    private int jwtExpirationMs = 86400000;
    public String generateJwtToken(Authentication authentication) {
        UserEntityDetails userPrincipal = (UserEntityDetails) authentication.getPrincipal();
        try {
            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .setId(userPrincipal.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Cannot generate JWT token: {}", e.getMessage());
        }
        return null;
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public String getJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }
    public String getIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getId();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}