package com.ds.app.jwtutil;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    private final String SECRET_KEY = "descartesdescartesdescartes12345";
    
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    public String extractUsername(String token) {
    	return extractClaim(token, claims->claims.getSubject());
    }
    
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    
    public Date extractExpiration(String token) {
    	return extractClaim(token, claims->claims.getExpiration());
    }
    
    private Claims extractAllClaims(String token) {
    	return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private Boolean isTokenExpired(String token) {
    	return extractExpiration(token).before(new Date());
    }
    public <T> T extractClaim(String token,Function<Claims,T> claimresolver) {
    	Claims cliams = extractAllClaims(token);
    	
    	return claimresolver.apply(cliams);
    }
        
        public String generateToken(String username, Long userId, String role) {
            return Jwts.builder()
                    .subject(username)
                    .claim("userId", userId)
                    .claim("role", role)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .signWith(key)
                    .compact();
        
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
}
