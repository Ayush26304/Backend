package com.kartcom.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartcom.auth.entity.Auth;

public interface UserRepo extends JpaRepository<Auth, Integer> {

	Auth findByUsername(String username);

}
