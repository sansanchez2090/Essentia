package com.essentia.auth.auth_service.tests;

import com.essentia.auth.auth_service.model.Role;
import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.repository.RoleRepository;
import com.essentia.auth.auth_service.repository.UserRepository;
import com.essentia.auth.auth_service.service.JwtService;
import com.essentia.auth.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userRole = new Role("ROLE_USER");
        userRole.setId(1L);
        
        adminRole = new Role("ROLE_ADMIN");
        adminRole.setId(2L);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setActive(true);
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

    // Act
    User result = userService.registerUser(testUser, null);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByName("ROLE_USER");
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(testUser, null);
        });
    }

    @Test
    void testLogin_Success() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("password123");
        testUser.setPassword(hashedPassword);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.login("test@example.com", "password123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testLogin_WrongPassword() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("password123");
        testUser.setPassword(hashedPassword);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.login("test@example.com", "wrongpassword");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.login("nonexistent@example.com", "password123");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testAddRoleToUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.addRoleToUser(1L, "ROLE_ADMIN");

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testIsUserAdmin_True() {
        // Arrange
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(userRole);
        adminRoles.add(adminRole);
        testUser.setRoles(adminRoles);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        boolean result = userService.isUserAdmin(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsUserAdmin_False() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        boolean result = userService.isUserAdmin(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deactivateUser(1L);

        // Assert
        assertFalse(testUser.isActive());
        verify(userRepository, times(1)).save(testUser);
    }
}
