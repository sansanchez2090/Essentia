package com.essentia.auth.auth_service.controller;

import com.essentia.auth.auth_service.model.UserProfile;
import com.essentia.auth.auth_service.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        Optional<UserProfile> profile = userProfileService.getProfileByUserId(userId);
        
        if (profile.isPresent()) {
            return ResponseEntity.ok(createProfileResponse(profile.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createOrUpdateProfile(
            @PathVariable Long userId, 
            @RequestBody UserProfileRequest request) {
        try {
            UserProfile profileDetails = new UserProfile();
            profileDetails.setFirstName(request.getFirstName());
            profileDetails.setLastName(request.getLastName());
            profileDetails.setPhone(request.getPhone());
            profileDetails.setAvatarUrl(request.getAvatarUrl());

            UserProfile savedProfile = userProfileService.createOrUpdateProfile(userId, profileDetails);
            return ResponseEntity.ok(createProfileResponse(savedProfile));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(
            @PathVariable Long userId, 
            @RequestBody AvatarUpdateRequest request) {
        try {
            UserProfile updatedProfile = userProfileService.updateAvatar(userId, request.getAvatarUrl());
            return ResponseEntity.ok(createProfileResponse(updatedProfile));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/completion")
    public ResponseEntity<?> getProfileCompletion(@PathVariable Long userId) {
        int completion = userProfileService.getProfileCompletionPercentage(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("completionPercentage", completion);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long userId) {
        try {
            userProfileService.deleteProfile(userId);
            return ResponseEntity.ok("Profile deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper method to create clean response without circular references
    private Map<String, Object> createProfileResponse(UserProfile profile) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", profile.getId());
        response.put("userId", profile.getUser().getId());
        response.put("firstName", profile.getFirstName());
        response.put("lastName", profile.getLastName());
        response.put("fullName", profile.getFullName());
        response.put("phone", profile.getPhone());
        response.put("avatarUrl", profile.getAvatarUrl());
        response.put("hasAvatar", profile.hasAvatar());
        response.put("profileComplete", profile.isProfileComplete());
        response.put("createdAt", profile.getCreatedAt());
        response.put("updatedAt", profile.getUpdatedAt());
        return response;
    }

    // Request DTO classes
    public static class UserProfileRequest {
        private String firstName;
        private String lastName;
        private String phone;
        private String avatarUrl;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }

    public static class AvatarUpdateRequest {
        private String avatarUrl;

        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }
}