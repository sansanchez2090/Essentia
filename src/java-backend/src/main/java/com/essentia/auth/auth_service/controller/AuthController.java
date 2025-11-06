package com.essentia.auth.auth_service.controller;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.service.JwtService;
import com.essentia.auth.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService; // Add this injection

    @GetMapping("/hello")
    public String hello() {
        return "Auth service is running";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());

            return ResponseEntity.ok(userService.registerUser(user, request.getRole()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        Optional<User> userOptional = userService.login(email, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String mainRole = user.getMainRole();

            // Generate JWT token
            String token = jwtService.generateToken(user.getEmail(), user.getId(), mainRole);

            // Create response with token
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("role", mainRole);
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("expiresIn", jwtService.extractExpiration(token));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        return ResponseEntity.ok(createUserResponses(users));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            Iterable<User> users = userService.getUsersByRole(normalizeRole(role));
            return ResponseEntity.ok(createUserResponses(users));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        return handleRoleUpdate(userId, body, true);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{userId}/role")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        return handleRoleUpdate(userId, body, false);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{userId}/is-admin")
    public ResponseEntity<?> isUserAdmin(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(Map.of("isAdmin", userService.isUserAdmin(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        return handleUserActivation(userId, false);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        return handleUserActivation(userId, true);
    }

    @GetMapping("/users/active")
    public ResponseEntity<?> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<?> getInactiveUsers() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    // ----------------- Helper Methods -----------------

    private String normalizeRole(String role) {
        if (role == null)
            return "ROLE_USER";
        role = role.toUpperCase().trim();
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }

    private List<Map<String, Object>> createUserResponses(Iterable<User> users) {
        List<Map<String, Object>> list = new ArrayList<>();
        users.forEach(user -> list.add(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getMainRole(),
                "active", user.isActive(),
                "createdAt", user.getCreatedAt(),
                "updatedAt", user.getUpdatedAt())));
        return list;
    }

    private ResponseEntity<?> handleRoleUpdate(Long userId, Map<String, String> body, boolean add) {
        String role = body.get("role");
        if (role == null)
            return ResponseEntity.badRequest().body("The 'role' field is required");
        try {
            User updated = add ? userService.addRoleToUser(userId, role)
                    : userService.removeRoleFromUser(userId, role);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ResponseEntity<?> handleUserActivation(Long userId, boolean activate) {
        try {
            if (activate)
                userService.activateUser(userId);
            else
                userService.deactivateUser(userId);
            return ResponseEntity.ok("User " + (activate ? "activated" : "deactivated") + " successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ----------------- DTOs -----------------
    public static class RegistrationRequest {
        private String username, email, password, role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static class LoginResponse {
        private final String message;
        private final Object roles;
        private final Long userId;
        private final String username;
        private final String email;

        public LoginResponse(String message, Object roles, Long userId, String username, String email) {
            this.message = message;
            this.roles = roles;
            this.userId = userId;
            this.username = username;
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public Object getRoles() {
            return roles;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
}
