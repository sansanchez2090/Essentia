package com.essentia.auth.auth_service.config;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.model.UserProfile;
import com.essentia.auth.auth_service.repository.UserProfileRepository;
import com.essentia.auth.auth_service.repository.UserRepository;
import com.essentia.auth.auth_service.repository.RoleRepository;
import com.essentia.auth.auth_service.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        // Only create users if they don't exist
        createUserIfNotExists("admin", "admin@example.com", "admin123", "ROLE_ADMIN", "System", "Administrator");
        createUserIfNotExists("testuser", "user@example.com", "user123", "ROLE_USER", "John", "Doe");
        createUserIfNotExists("dummyUser", "user@gmail.com", "user123", "ROLE_USER", "Josh", "Doe");
    }

    private void createRoles() {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            System.out.println("Role created: " + roleName);
        }
    }

    private void createUserIfNotExists(String username, String email, String password, 
                                     String roleName, String firstName, String lastName) {
        // Check if user exists by email OR username
        boolean userExists = userRepository.existsByEmail(email) || userRepository.existsByUsername(username);
        
        if (!userExists) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);

            // Assign role
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            
            // If admin, also add USER role
            if ("ROLE_ADMIN".equals(roleName)) {
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
                roles.add(userRole);
            }
            
            user.setRoles(roles);

            User savedUser = userRepository.save(user);

            // Create profile
            UserProfile profile = new UserProfile();
            profile.setUser(savedUser);
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setPhone("+1-555-0100");
            
            userProfileRepository.save(profile);
            
            System.out.println("User created: " + email + " / " + password);
        } else {
            System.out.println("User already exists: " + email);
        }
    }
}