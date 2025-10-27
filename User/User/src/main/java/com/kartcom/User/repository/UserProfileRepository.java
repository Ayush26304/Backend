package com.kartcom.User.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartcom.User.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

	Optional<UserProfile> findByUsername(String username);

}
