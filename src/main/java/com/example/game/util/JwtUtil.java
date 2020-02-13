package com.example.game.util;

import com.example.game.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static String k = "123712983781293782193721893712893";

    public static String createJwt(User user) {
        Key key = new SecretKeySpec(k.getBytes(), "HmacSHA256");

        Map<String, String> claims = new HashMap<>();
        claims.put("userId", Integer.toString(user.getId()));

        String jwt = Jwts.builder()
                .signWith(key)
                .setClaims(claims)
                .setIssuer("game")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400 * 1000))
                .setSubject(user.getName())
                .compact();

        return jwt;
    }

    public static Claims verifyJwt(String jwt) {
        if (jwt == null || jwt.equals("")) {
            return null;
        }

        Key key = new SecretKeySpec(k.getBytes(), "HmacSHA256");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (JwtException e) {
            return null;
        }
    }
}
