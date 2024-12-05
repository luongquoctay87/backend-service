package vn.tayjava.service;

import org.springframework.security.core.GrantedAuthority;
import vn.tayjava.common.TokenType;

import java.util.Collection;
import java.util.List;

public interface JwtService {

    String generateAccessToken(String username, List<String> authorities);

    String generateRefreshToken(String username, List<String> authorities);

    String extractUsername(String token, TokenType type);
}
