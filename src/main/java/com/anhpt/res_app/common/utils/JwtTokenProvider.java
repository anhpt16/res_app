package com.anhpt.res_app.common.utils;

import com.anhpt.res_app.common.dto.response.auth.RoleInfo;
import com.anhpt.res_app.common.dto.response.auth.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidityInMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = Base64.getDecoder().decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateAccessToken(UserInfo userInfo, List<RoleInfo> roleInfos) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidityInMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userInfo);
        claims.put("roles", roleInfos);

        return Jwts.builder()
            .setSubject(userInfo.getId().toString())
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public UserInfo getUserInfo(String token) {
        Claims claims = parseClaims(token);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(claims.get("user"), UserInfo.class);
    }

    public List<RoleInfo> getRoleInfos(String token) {
        Claims claims = parseClaims(token);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(
            claims.get("roles"),
            new TypeReference<List<RoleInfo>>() {}
        );
    }
}
