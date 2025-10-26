package com.kartcom.User.service;

import java.util.List;

import com.kartcom.User.dto.MinimalProfileDto;
import com.kartcom.User.dto.UpdateProfileDto;
import com.kartcom.User.dto.UserProfileDto;
import com.kartcom.User.entities.UserProfile;

public interface UserProfileService {
    List<UserProfileDto> getAllUsers();
    UserProfileDto getUserById(int id);
    UserProfileDto getUserByUsername(String username);
    UserProfile updateUser(int id, UpdateProfileDto dto);
    void createMinimalProfile(MinimalProfileDto dto);
    void deleteUser(int id);

}

