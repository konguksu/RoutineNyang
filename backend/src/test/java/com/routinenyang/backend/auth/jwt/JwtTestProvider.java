package com.routinenyang.backend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JwtTestProvider {
    @Test
    public void createTestToken() {
        System.out.println("test_user@gmail.com: " + createToken("test_user@gmail.com"));
        System.out.println("rndbsk13@gmail.com: " + createToken("rndbsk13@gmail.com"));
    }

    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 99999999))
                .signWith(Keys.hmacShaKeyFor("U29tZVN1cGVyU2VjdXJlS2V5Rm9yUm91dGluZU55YW5nMjAyNQ==".getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
