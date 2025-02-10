package com.maximogiordano.users.utils;

import com.maximogiordano.users.entity.InvalidToken;
import com.maximogiordano.users.repository.InvalidTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private final Key key;
    private final long expiration;
    private final InvalidTokenRepository invalidTokenRepository;

    public JwtUtils(@Value("${jwt.utils.key}") String key, @Value("${jwt.utils.expiration}") long expiration,
                    InvalidTokenRepository invalidTokenRepository) {

        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.expiration = expiration;
        this.invalidTokenRepository = invalidTokenRepository;
    }

    /**
     * Generates a new access token for the given user.
     */
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * Invalidates the given token.
     */
    public void invalidateToken(String token) {
        var invalidToken = new InvalidToken();
        invalidToken.setValue(token);
        invalidTokenRepository.save(invalidToken);
    }

    /**
     * Validates the given token (check if it's valid and hasn't been invalidated)
     */
    public boolean validateToken(String token) {
        if (invalidTokenRepository.existsById(token)) {
            return false;  // Token has been invalidated
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true; // Token is valid
        } catch (JwtException e) {
            return false; // Invalid token
        }
    }

    /**
     * Extracts the username from the given token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
