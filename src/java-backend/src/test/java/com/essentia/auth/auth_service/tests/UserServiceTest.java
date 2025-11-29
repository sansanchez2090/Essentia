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
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerUser(testUser, null);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
        verify(roleRepository).findByName("ROLE_USER");
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(testUser, null);
        });
    }

    @Test
    void testLogin_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("password123");
        testUser.setPassword(hashed);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.login("test@example.com", "password123");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testLogin_WrongPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        testUser.setPassword(encoder.encode("password123"));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.login("test@example.com", "wrongpassword");

        assertFalse(result.isPresent());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("nonexistent@example.com", "password123");

        assertFalse(result.isPresent());
    }

    @Test
    void testAddRoleToUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.addRoleToUser(1L, "ROLE_ADMIN");

        assertNotNull(result);
        assertTrue(testUser.getRoles().contains(adminRole));
        verify(userRepository).save(testUser);
    }

    @Test
    void testIsUserAdmin_True() {
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        testUser.setRoles(roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.isUserAdmin(1L);

        assertTrue(result);
    }

    @Test
    void testIsUserAdmin_False() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.isUserAdmin(1L);

        assertFalse(result);
    }

    @Test
    void testDeactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.deactivateUser(1L);

        assertFalse(testUser.isActive());
        verify(userRepository).save(testUser);
    }
}
