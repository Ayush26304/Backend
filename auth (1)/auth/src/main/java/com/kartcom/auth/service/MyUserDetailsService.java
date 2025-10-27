package com.kartcom.auth.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kartcom.auth.entity.Auth;
import com.kartcom.auth.entity.UserPrincipal;
import com.kartcom.auth.repository.UserRepo;



/**
 * Custom implementation of UserDetailsService to load user details from the database.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Auth user = repo.findByUsername(username);

        if (user == null) {
            // Optional: log warning or error
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new UserPrincipal(user);
    }
}
