package com.essentia.auth.auth_service.service;

import com.essentia.auth.auth_service.model.User;
import com.essentia.auth.auth_service.model.UserProfile;
import com.essentia.auth.auth_service.repository.UserProfileRepository;
import com.essentia.auth.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserProfile createOrUpdateProfile(Long userId, UserProfile profileDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Optional<UserProfile> existingProfile = userProfileRepository.findByUser(user);

        if (existingProfile.isPresent()) {
            // Update existing profile
            UserProfile profile = existingProfile.get();
            updateProfileFields(profile, profileDetails);
            return userProfileRepository.save(profile);
        } else {
            // Create new profile
            UserProfile newProfile = new UserProfile();
            newProfile.setUser(user);
            updateProfileFields(newProfile, profileDetails);
            return userProfileRepository.save(newProfile);
        }
    }

    private void updateProfileFields(UserProfile target, UserProfile source) {
        if (source.getFirstName() != null) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getLastName() != null) {
            target.setLastName(source.getLastName());
        }
        if (source.getPhone() != null) {
            target.setPhone(source.getPhone());
        }
        if (source.getAvatarUrl() != null) {
            target.setAvatarUrl(source.getAvatarUrl());
        }
    }

    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public Optional<UserProfile> getProfileByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    @Transactional
    public UserProfile updateAvatar(Long userId, String avatarUrl) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId));

        profile.setAvatarUrl(avatarUrl);
        return userProfileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId));
        userProfileRepository.delete(profile);
    }

    public boolean profileExists(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }

    public int getProfileCompletionPercentage(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .map(this::calculateCompletionPercentage)
                .orElse(0);
    }

    private int calculateCompletionPercentage(UserProfile profile) {
        int totalFields = 3; // firstName, lastName, phone
        int completedFields = 0;

        if (profile.getFirstName() != null && !profile.getFirstName().trim().isEmpty())
            completedFields++;
        if (profile.getLastName() != null && !profile.getLastName().trim().isEmpty())
            completedFields++;
        if (profile.getPhone() != null && !profile.getPhone().trim().isEmpty())
            completedFields++;

        // Use floating point division and round to nearest integer
        return (int) Math.round((completedFields * 100.0) / totalFields);
    }
}