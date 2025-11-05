package com.essentia.auth.auth_service.service;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // TODO: Add password hashing and validation
        System.out.println("Usuario recibido: " + user.getEmail() + " - " + user.getUsername() + "-" + user.getPassword());
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
