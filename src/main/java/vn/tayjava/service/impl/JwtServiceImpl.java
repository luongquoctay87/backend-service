package vn.tayjava.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import vn.tayjava.common.TokenType;
import vn.tayjava.exception.InvalidDataException;
import vn.tayjava.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static vn.tayjava.common.TokenType.ACCESS_TOKEN;
import static vn.tayjava.common.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryMinutes:60}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay:14}")
    private long expiryDay;

    @Value("${jwt.accessKey:dummy-access-key}")
    private String accessKey;

    @Value("${jwt.refreshKey:dummy-refresh-key}")
    private String refreshKey;

    @Override
    public String generateAccessToken(String username, List<String> authorities) {
        log.info("Generate access token for user {} with authorities {}", username, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return createAccessToken(claims, username);
    }

    @Override
    public String generateRefreshToken(String username, List<String> authorities) {
        log.info("Generate refresh token");

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return createRefreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private String createAccessToken(Map<String, Object> claims, String username) {
        log.info("Create access token for user {}", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String username) {
        log.info("Create refresh token for user {}", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        log.info("Create key for type {}", type);

        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        log.info("Extract claim for token {}...", token.substring(0, 15));

        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        log.info("Extract all claims for token {}...", token.substring(0, 15));
        try {
            return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }
}
