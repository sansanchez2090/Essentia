package com.essentia.auth.auth_service.repository;

import com.essentia.auth.auth_service.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    // Find users by role
    Iterable<User> findByRoles_Name(String roleName);
    
    // Methods for active/inactive status
    Iterable<User> findByIsActiveTrue();
    Iterable<User> findByIsActiveFalse();
}