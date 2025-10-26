package com.kartcom.User.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartcom.User.dto.MinimalProfileDto;
import com.kartcom.User.dto.UpdateProfileDto;
import com.kartcom.User.dto.UserProfileDto;
import com.kartcom.User.entities.UserProfile;
import com.kartcom.User.repository.UserProfileRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository repo;

    @Override
    public List<UserProfileDto> getAllUsers() {
        List<UserProfile> users = repo.findAll();
        List<UserProfileDto> dtos = new ArrayList<>();

        for (UserProfile user : users) {
            UserProfileDto dto = new UserProfileDto();
            BeanUtils.copyProperties(user, dto);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public UserProfileDto getUserById(int id) {
        UserProfile user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserProfileDto getUserByUsername(String username) {
        UserProfile user = repo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserProfile updateUser(int id, UpdateProfileDto dto) {
        UserProfile user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        user.setPhone(dto.getPhone());
        user.setFullName(dto.getFullName());
        user.setAddressLine1(dto.getAddressLine1());
        user.setAddressLine2(dto.getAddressLine2());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());
        user.setUpdatedAt(LocalDateTime.now());

        return repo.save(user);
    }

    @Override
    public void deleteUser(int id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public void createMinimalProfile(MinimalProfileDto dto) {
        if (repo.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Profile already exists for username: " + dto.getUsername());
        }

        UserProfile profile = new UserProfile();
        profile.setUsername(dto.getUsername());
        profile.setRole(dto.getRole());
        profile.setActive(true);
        profile.setCreatedAt(LocalDateTime.now());

        repo.save(profile);
    }
}
