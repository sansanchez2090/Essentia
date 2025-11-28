package com.essentia.auth.auth_service.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.essentia.auth.auth_service.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        try {
            // Secret usado SOLO en tests
            var field = JwtService.class.getDeclaredField("secret");
            field.setAccessible(true);
            field.set(jwtService, "secretkeyforunittestsessentiaengsoftware");

            // Expiración de prueba
            var expirationField = JwtService.class.getDeclaredField("expiration");
            expirationField.setAccessible(true);
            expirationField.set(jwtService, 86400000L); // 24 hrs
        } catch (Exception e) {
            fail("Failed to set secret or expiration field: " + e.getMessage());
        }
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        String username = jwtService.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractUserId() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        Long userId = jwtService.extractUserId(token);

        assertEquals(1L, userId);
    }

    @Test
    void testExtractRole() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        String role = jwtService.extractRole(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        boolean isValid = jwtService.validateToken(token, "test@example.com");

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        boolean isValid = jwtService.validateToken(token, "wrong@example.com");

        assertFalse(isValid);
    }

    @Test
    void testValidateToken_GeneralValidation() {
        String token = jwtService.generateToken("test@example.com", 1L, "ADMIN");

        boolean isValid = jwtService.validateToken(token);

        assertTrue(isValid);
    }
}
