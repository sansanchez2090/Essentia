package com.essentia.auth.auth_service.repository;

import com.essentia.auth.auth_service.model.Role;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}