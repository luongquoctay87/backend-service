package vn.tayjava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import vn.tayjava.common.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtServiceTest {

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAccessToken_Success() {
        // Chuẩn bị dữ liệu
        String username = "john.doe";
        List<String> authorities = List.of("USER", "ADMIN");
        String expectedToken = "mockAccessToken";

        // Giả lập phương thức phụ createAccessToken
        when(jwtService.generateAccessToken(username, authorities)).thenReturn(expectedToken);

        // Gọi phương thức cần kiểm tra
        String actualToken = jwtService.generateAccessToken(username, authorities);

        // Kiểm tra kết quả
        assertEquals(expectedToken, actualToken);
        verify(jwtService, times(1)).generateAccessToken(username, authorities);
    }

    @Test
    void generateRefreshToken() {
        // Chuẩn bị dữ liệu
        String username = "john.doe";
        List<String> authorities = List.of("USER", "ADMIN");
        String expectedToken = "mockAccessToken";

        // Giả lập phương thức phụ createAccessToken
        when(jwtService.generateRefreshToken(username, authorities)).thenReturn(expectedToken);

        // Gọi phương thức cần kiểm tra
        String actualToken = jwtService.generateRefreshToken(username, authorities);

        // Kiểm tra kết quả
        assertEquals(expectedToken, actualToken);
        verify(jwtService, times(1)).generateRefreshToken(username, authorities);
    }

    @Test
    void testExtractUsername_Success() {
        // Chuẩn bị dữ liệu
        String token = "mockToken";
        TokenType tokenType = TokenType.ACCESS_TOKEN;
        String expectedUsername = "john.doe";

        // Giả lập hành vi của extractClaim
        when(jwtService.extractUsername(token, tokenType)).thenReturn(expectedUsername);

        // Gọi phương thức cần kiểm tra
        String actualUsername = jwtService.extractUsername(token, tokenType);

        // Kiểm tra kết quả
        assertEquals(expectedUsername, actualUsername);
        verify(jwtService, times(1)).extractUsername(token, tokenType);
    }
}