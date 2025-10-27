package com.kartcom.auth.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserPrincipal implements UserDetails {

    private final Auth user;

    public UserPrincipal(Auth user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Ensure role is prefixed with "ROLE_" to comply with Spring Security conventions
        String role = user.getRole();
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize if needed
    }

    public String getRole() {
        return user.getRole();
    }

    public int getId() {
        return user.getId();
    }

    public Auth getUser() {
        return user;
    }
}
