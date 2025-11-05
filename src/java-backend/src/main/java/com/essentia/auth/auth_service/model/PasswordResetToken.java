package com.essentia.auth.auth_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String token;

    private LocalDateTime expiresAt;
    private boolean used = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
}
