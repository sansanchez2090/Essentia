package com.essentia.auth.auth_service.service;

import com.essentia.auth.auth_service.model.Role;
import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.model.UserRole;
import com.essentia.auth.auth_service.repository.RoleRepository;
import com.essentia.auth.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
public User registerUser(User user, String requestedRole) {
    // Basic validations
    if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
        throw new IllegalArgumentException("Email is required");
    }
    if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
        throw new IllegalArgumentException("Password is required");
    }
    if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
        throw new IllegalArgumentException("Username is required");
    }

    // Check if email already exists
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new IllegalArgumentException("Email is already registered");
    }

    // Check if username already exists
    if (userRepository.existsByUsername(user.getUsername())) {
        throw new IllegalArgumentException("Username is already taken");
    }

    // Hash password
    String hashedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(hashedPassword);

    // Determine role - use requested role or default to USER
    UserRole finalRole = determineUserRole(requestedRole);
    
    // Assign corresponding roles in the many-to-many relationship
    Set<Role> roles = assignRolesBasedOnUserRole(finalRole);
    user.setRoles(roles);

    System.out.println("Registering user: " + user.getEmail() + 
                      " - " + user.getUsername() + 
                      " - Role: " + finalRole);

    return userRepository.save(user);
}

private UserRole determineUserRole(String requestedRole) {
    if (requestedRole == null || requestedRole.trim().isEmpty()) {
        return UserRole.USER; // Default role
    }
    
    try {
        return UserRole.valueOf(requestedRole.toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid role: " + requestedRole + 
                                         ". Valid roles: " + Arrays.toString(UserRole.values()));
    }
}

private Set<Role> assignRolesBasedOnUserRole(UserRole userRole) {
    Set<Role> roles = new HashSet<>();
    
    switch (userRole) {
        case ADMIN:
            // Admin gets both ADMIN and USER roles
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ROLE_ADMIN not found"));
            Role userRoleEntity = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found"));
            roles.add(adminRole);
            roles.add(userRoleEntity);
            break;
            
        case USER:
        default:
            // Regular user gets only USER role
            Role defaultUserRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role ROLE_USER not found"));
            roles.add(defaultUserRole);
            break;
    }
    
    return roles;
}

    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verify password with hash
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Methods for role management
    @Transactional
    public User addRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.addRole(role);
        return userRepository.save(user);
    }

    @Transactional
    public User removeRoleFromUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.removeRole(role);
        return userRepository.save(user);
    }

    public Iterable<User> getUsersByRole(String roleName) {
        return userRepository.findByRoles_Name(roleName);
    }

    public boolean userHasRole(Long userId, String roleName) {
        return userRepository.findById(userId)
                .map(user -> user.hasRole(roleName))
                .orElse(false);
    }

    public boolean isUserAdmin(Long userId) {
        return userHasRole(userId, "ROLE_ADMIN");
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setActive(true);
        userRepository.save(user);
    }

    public Iterable<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public Iterable<User> getInactiveUsers() {
        return userRepository.findByIsActiveFalse();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}