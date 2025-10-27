package com.kartcom.order.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
 
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    private final JwtUtil jwtUtil;
 
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	System.out.println("==============6666===");
        String authHeader=request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token=authHeader.substring(7);
            try{
            	System.out.println("=============5555====");
                String username=jwtUtil.extractName(token);
                System.out.println("=================");
                List<String> roles=jwtUtil.extractRole(token);
                System.out.println("===88888888888888");
                Integer userId = jwtUtil.extractUserId(token);
                List<GrantedAuthority> authorities=roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                System.out.println("===87777777777777");
                Authentication authentication=new UsernamePasswordAuthenticationToken(String.valueOf(userId),null,authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("============4444444=====");
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}