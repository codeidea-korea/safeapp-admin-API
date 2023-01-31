package com.safeapp.admin.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${encrypt.jwt.key}")
    public String jwtKey;

    public final static long ACCESS_TOKEN_ALIVE_TIME = 1000L * 60 * 60 * 1;
    public final static long REFRESH_TOKEN_ALIVE_TIME = 1000L * 60 * 60 * 1;

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Admins admin, long expiredTime) {
        Claims clm = Jwts.claims();

        clm.put("id", admin.getAdminId());
        clm.put("s", admin.getId());
        clm.put("m", admin.getEmail());

        return
            Jwts.builder()
            .setClaims(clm)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(getSigningKey(jwtKey), SignatureAlgorithm.HS512)
            .compact();
    }
    public String generateAccessToken(Admins admin) {
        return generateAccessToken(admin, ACCESS_TOKEN_ALIVE_TIME);
    }

    public String generateRefreshToken(Admins admin, long expiredTime) {
        Claims clm = Jwts.claims();

        clm.put("sem", admin.getAdminId());
        clm.put("lt", expiredTime);
        clm.put("dm", REFRESH_TOKEN_ALIVE_TIME);

        return
                Jwts.builder()
                .setClaims(clm)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSigningKey(jwtKey), SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateRefreshToken(Admins admin) {
        return generateRefreshToken(admin, REFRESH_TOKEN_ALIVE_TIME);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return
                Jwts.parserBuilder()
                .setSigningKey(getSigningKey(jwtKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAdminIDOrUserIdByAccessToken(String token) {
        final Claims claims = extractAllClaims(token);

        return (String)claims.get("id");
    }

    public String getAdminIDOrUserIdByRefreshToken(String token) {
        final Claims claims = extractAllClaims(token);

        return (String)claims.get("sem");
    }

    public boolean isExpired(String token) {
        final Claims claims = extractAllClaims(token);

        if(Objects.isNull(claims) || Objects.isNull(claims.getExpiration())) {
            return true;
        }

        return claims.getExpiration().before(new Date());
    }

}