package com.essentia.auth.auth_service.tests;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.essentia.auth.auth_service.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Use reflection to set the secret for testing
        try {
            var field = JwtService.class.getDeclaredField("secret");
            field.setAccessible(true);
            field.set(jwtService, "secretkeyforunittestsessentiaengsoftware");
            
            var expirationField = JwtService.class.getDeclaredField("expiration");
            expirationField.setAccessible(true);
            expirationField.set(jwtService, 86400000L); // 24 hours
        } catch (Exception e) {
            fail("Failed to set secret field: " + e.getMessage());
        }
    }

    @Test
    void testGenerateToken() {
        // Act
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractUserId() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        Long userId = jwtService.extractUserId(token);

        // Assert
        assertEquals(1L, userId);
    }

    @Test
    void testExtractRole() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        String role = jwtService.extractRole(token);

        // Assert
        assertEquals("ADMIN", role);
    }

    @Test
    void testValidateToken_Valid() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        boolean isValid = jwtService.validateToken(token, "test@example.com");

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        boolean isValid = jwtService.validateToken(token, "wrong@example.com");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_GeneralValidation() {
        // Arrange
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        // Act
        boolean isValid = jwtService.validateToken(token);

        // Assert
        assertTrue(isValid);
    }
}