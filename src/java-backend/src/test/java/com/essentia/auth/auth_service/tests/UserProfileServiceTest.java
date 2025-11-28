package com.essentia.auth.auth_service.tests;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.model.UserProfile;
import com.essentia.auth.auth_service.repository.UserProfileRepository;
import com.essentia.auth.auth_service.repository.UserRepository;
import com.essentia.auth.auth_service.service.UserProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User testUser;
    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testProfile = new UserProfile();
        testProfile.setId(1L);
        testProfile.setUser(testUser);
        testProfile.setFirstName("John");
        testProfile.setLastName("Doe");
        testProfile.setPhone("+1234567890");
    }

    @Test
    void testCreateOrUpdateProfile_CreateNew() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // Act
        UserProfile result = userProfileService.createOrUpdateProfile(1L, testProfile);

        // Assert
        assertNotNull(result);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testCreateOrUpdateProfile_UpdateExisting() {
        // Arrange
        UserProfile existingProfile = new UserProfile();
        existingProfile.setId(1L);
        existingProfile.setUser(testUser);
        existingProfile.setFirstName("Old");
        existingProfile.setLastName("Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // Act
        UserProfile result = userProfileService.createOrUpdateProfile(1L, testProfile);

        // Assert
        assertNotNull(result);
        verify(userProfileRepository, times(1)).save(existingProfile);
    }

    @Test
    void testGetProfileByUserId_Found() {
        // Arrange
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        // Act
        Optional<UserProfile> result = userProfileService.getProfileByUserId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetProfileByUserId_NotFound() {
        // Arrange
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act
        Optional<UserProfile> result = userProfileService.getProfileByUserId(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateAvatar_Success() {
        // Arrange
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        // Act
        UserProfile result = userProfileService.updateAvatar(1L, "https://example.com/new-avatar.jpg");

        // Assert
        assertNotNull(result);
        assertEquals("https://example.com/new-avatar.jpg", testProfile.getAvatarUrl());
        verify(userProfileRepository, times(1)).save(testProfile);
    }

    @Test
    void testGetProfileCompletionPercentage_Full() {
        // Arrange
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        // Act
        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        // Assert
        assertEquals(100, percentage);
    }

    @Test
    void testGetProfileCompletionPercentage_Partial() {
        // Arrange
        testProfile.setPhone(null);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        // Act
        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        // Assert
        assertEquals(67, percentage); // 2 out of 3 fields completed
    }

    @Test
    void testGetProfileCompletionPercentage_NoProfile() {
        // Arrange
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act
        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        // Assert
        assertEquals(0, percentage);
    }
}