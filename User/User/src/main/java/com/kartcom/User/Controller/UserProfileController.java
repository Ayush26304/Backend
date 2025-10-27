package com.kartcom.User.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kartcom.User.dto.UpdateProfileDto;
import com.kartcom.User.dto.UserProfileDto;
import com.kartcom.User.entities.UserProfile;
import com.kartcom.User.feign.AuthFeignClient;
import com.kartcom.User.service.UserProfileService;

/**
 * REST controller for managing user profiles.
 */
@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @Autowired
    private UserProfileService service;
    
    @GetMapping("/hi")
	String hi() {
		System.out.println("hi");
		return "hi";
	}

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileDto> getUserByUsername(Authentication auth) {
        return ResponseEntity.ok(service.getUserByUsername(auth.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> updateUser(@PathVariable int id,
                                                  @RequestBody UpdateProfileDto dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @Autowired
    private AuthFeignClient authFeignClient;

    @GetMapping("/secure-check")
    public ResponseEntity<String> checkToken(@RequestHeader("Authorization") String token) {
        Boolean isValid = authFeignClient.validateToken(token).getBody();
        return isValid ? ResponseEntity.ok("Token valid") : ResponseEntity.status(401).body("Invalid token");
    }

}
