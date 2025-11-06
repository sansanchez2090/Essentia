package com.essentia.auth.auth_service.repository;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.model.UserProfile;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUser(User user);
    boolean existsByUserId(Long userId);
}