package com.kartcom.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kartcom.auth.dto.LoginDto;
import com.kartcom.auth.dto.UserDto;
import com.kartcom.auth.entity.Auth;
import com.kartcom.auth.service.JwtService;
import com.kartcom.auth.service.MyUserDetailsService;
import com.kartcom.auth.service.UserService;

/**
 * REST controller for authentication and role-based access endpoints.
 */
@RestController
@RequestMapping("/api/auth")
//@CrossOrigin
public class SecurityController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwt;
    
    @Autowired
    private MyUserDetailsService ser;

    /**
     * Registers a new user.
     */
    @PostMapping("/adduser")
    public ResponseEntity<?> adduser(@RequestBody UserDto userDto) {
        try {
            Auth savedUser = service.adduser(userDto);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed");
        }
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String role = authentication.getAuthorities().stream()
                        .findFirst()
                        .map(auth -> auth.getAuthority())
                        .orElse("ROLE_USER");

                String token = jwt.tokenGenerator(authentication);
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
//

    
    
    /**
     * Accessible only by users with ROLE_USER.
     */
    @GetMapping("/hello")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Hello USER");
    }

    /**
     * Accessible only by users with ROLE_ADMIN.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Hello ADMIN");
    }
}
