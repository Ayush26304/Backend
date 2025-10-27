package com.kartcom.catalog.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kartcom.catalog.security.JwtAuthFilter;


 
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
 
    private final JwtAuthFilter jwtAuthFilter;
 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(c->c.disable())
                .authorizeHttpRequests(auth->auth
                		.requestMatchers("/api/catalog/product/{id}").permitAll()
                		.requestMatchers( "/api/catalog/product/reduce-stock/{id}").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
