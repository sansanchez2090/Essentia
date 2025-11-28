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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        UserProfile result = userProfileService.createOrUpdateProfile(1L, testProfile);

        assertNotNull(result);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testCreateOrUpdateProfile_UpdateExisting() {
        UserProfile existingProfile = new UserProfile();
        existingProfile.setId(1L);
        existingProfile.setUser(testUser);
        existingProfile.setFirstName("Old");
        existingProfile.setLastName("Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userProfileRepository.findByUser(testUser)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        UserProfile result = userProfileService.createOrUpdateProfile(1L, testProfile);

        assertNotNull(result);
        verify(userProfileRepository, times(1)).save(existingProfile);
    }

    @Test
    void testGetProfileByUserId_Found() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        Optional<UserProfile> result = userProfileService.getProfileByUserId(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetProfileByUserId_NotFound() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Optional<UserProfile> result = userProfileService.getProfileByUserId(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateAvatar_Success() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testProfile);

        UserProfile result = userProfileService.updateAvatar(1L, "https://example.com/new-avatar.jpg");

        assertNotNull(result);
        assertEquals("https://example.com/new-avatar.jpg", testProfile.getAvatarUrl());
        verify(userProfileRepository, times(1)).save(testProfile);
    }

    @Test
    void testGetProfileCompletionPercentage_Full() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        assertEquals(100, percentage);
    }

    @Test
    void testGetProfileCompletionPercentage_Partial() {
        testProfile.setPhone(null);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(testProfile));

        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        assertEquals(67, percentage); // 2 of 3 fields
    }

    @Test
    void testGetProfileCompletionPercentage_NoProfile() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        int percentage = userProfileService.getProfileCompletionPercentage(1L);

        assertEquals(0, percentage);
    }
}
