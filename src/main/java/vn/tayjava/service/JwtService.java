package vn.tayjava.service;

import vn.tayjava.common.TokenType;

import java.util.List;

public interface JwtService {

    String generateAccessToken(String username, List<String> authorities);

    String generateRefreshToken(String username, List<String> authorities);

    String extractUsername(String token, TokenType type);
}
