package com.essentia.auth.auth_service.model;

public enum UserRole {
    USER,
    ADMIN;

    /**
     * Safely converts a string to a UserRole
     */
    public static UserRole fromString(String role) {
        if (role == null) {
            return USER; // Default value
        }
        
        try {
            return UserRole.valueOf(role.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return USER;
        }
    }

    public static boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }
        
        try {
            UserRole.valueOf(role.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getDisplayName() {
        switch (this) {
            case ADMIN:
                return "Administrator";
            case USER:
            default:
                return "User";
        }
    }

    /**
     * Returns all available roles as an array of strings
     */
    public static String[] getAvailableRoles() {
        UserRole[] roles = values();
        String[] roleNames = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].name();
        }
        return roleNames;
    }
}