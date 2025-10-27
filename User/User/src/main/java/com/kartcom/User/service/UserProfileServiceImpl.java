package com.kartcom.User.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartcom.User.dto.UpdateProfileDto;
import com.kartcom.User.dto.UserProfileDto;
import com.kartcom.User.entities.UserProfile;
import com.kartcom.User.repository.UserProfileRepository;
import com.kartcom.User.service.UserProfileService;

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
        Optional<UserProfile> optionalUser = repo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(optionalUser.get(), dto);
        return dto;
    }

    @Override
    public UserProfileDto getUserByUsername(String username) {
        Optional<UserProfile> optionalUser = repo.findByUsername(username);
//        if (!optionalUser.isPresent()) {
//            throw new EntityNotFoundException("User not found with username: " + username);
//        }

        UserProfileDto dto = new UserProfileDto();
        BeanUtils.copyProperties(optionalUser.get(), dto);
        return dto;
    }

    @Override
    public UserProfile updateUser(int id, UpdateProfileDto dto) {
        Optional<UserProfile> optionalUser = repo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        UserProfile user = optionalUser.get();
        user.setPhone(dto.getPhone());
        user.setFullName(dto.getFullName());
        user.setAddressLine1(dto.getAddressLine1());
        user.setAddressLine2(dto.getAddressLine2());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());

        return repo.save(user);
    }

    @Override
    public void deleteUser(int id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        repo.deleteById(id);
    }
}
