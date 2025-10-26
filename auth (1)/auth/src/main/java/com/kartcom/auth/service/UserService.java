package com.kartcom.auth.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kartcom.auth.dto.MinimalProfileDto;
import com.kartcom.auth.dto.UserDto;
import com.kartcom.auth.entity.Auth;
import com.kartcom.auth.feign.UserProfileClient;
import com.kartcom.auth.repository.UserRepo;



/**
 * Service for user registration and persistence.
 */
@Service
public class UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private UserProfileClient userProfileClient;


    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user with encoded password and validated role.
     */
//    public Auth adduser(UserDto userDto) {
//        Auth user = new Auth();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(bcrypt.encode(userDto.getPassword()));
//
//        // Ensure role is prefixed with "ROLE_" and defaults to "ROLE_USER" if missing
//        String role = userDto.getRole();
//        if (role == null || role.isBlank()) {
//            role = "ROLE_USER";
//        } else if (!role.startsWith("ROLE_")) {
//            role = "ROLE_" + role.toUpperCase();
//        }
//        user.setRole(role);
//        
//
//        return repo.save(user);
//    }
    public Auth adduser(UserDto userDto) {
        Auth user = new Auth();
        user.setUsername(userDto.getUsername());
        user.setPassword(bcrypt.encode(userDto.getPassword()));

        // Ensure role is prefixed with "ROLE_" and defaults to "ROLE_USER" if missing
        String role = userDto.getRole();
        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        } else if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }
        user.setRole(role);

        Auth savedUser = repo.save(user);

        // 🔗 Call User Service to create minimal profile
        MinimalProfileDto profileDto = new MinimalProfileDto(savedUser.getUsername(), savedUser.getRole());
        try {
            userProfileClient.createMinimalProfile(profileDto);
        } catch (Exception e) {
            // Optional: log error or handle fallback
            System.err.println("Failed to create minimal profile: " + e.getMessage());
        }

        return savedUser;
    }

}
