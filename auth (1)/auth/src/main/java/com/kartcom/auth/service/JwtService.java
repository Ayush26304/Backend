//package com.kartcom.auth.service;
//
//
//
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import com.kartcom.auth.entity.*;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
//
///**
// * Service for generating and validating JWT tokens.
// */
//@Service
//public class JwtService {
//
//    // Static secret for fallback (not recommended for production)
//   
//
//    // Dynamically generated secret key
//	@Value("${app.security.secretKey}")
//    private  String secretKey;
//	
//	 public SecretKey key(){
//	        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//	    }
//
////    public JwtService() {
////        this.secretKey = generateSecretKey();
////    }
//
//    /**
//     * Generates a secure HMAC SHA-256 secret key.
//     */
////    private String generateSecretKey() {
////        try {
////            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
////            SecretKey secretKey = keyGen.generateKey();
////            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
////        } catch (NoSuchAlgorithmException e) {
////            throw new RuntimeException("Error generating secret key", e);
////        }
////    }
//
//    /**
//     * Generates a JWT token with username and role claims.
//     */
//    public String generatetoken(String username, String role) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", role); // Embed role into token
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3)) // 3 minutes
//                .signWith(key())
//                .compact();
//    }
//
//    /**
//     * Returns the signing key used for token generation and validation.
//     */
//    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    /**
//     * Extracts the username (subject) from the token.
//     */
//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    /**
//     * Extracts the role claim from the token.
//     */
//    public String extractUserRole(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("role", String.class);
//    }
//
//    /**
//     * Generic claim extractor.
//     */
//    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    /**
//     * Parses and returns all claims from the token.
//     */
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /**
//     * Validates the token against the user details.
//     */
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    /**
//     * Checks if the token is expired.
//     */
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    /**
//     * Extracts the expiration date from the token.
//     */
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//}

package com.kartcom.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.kartcom.auth.entity.Auth;
import com.kartcom.auth.repository.UserRepo;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
@Component
public class JwtService {
    @Value("${app.security.secretKey}")
    private String secretKey;
    @Value("${app.security.expiray}")
    private long expiration;
    @Autowired
    UserRepo urepo;
 
    public SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String tokenGenerator(Authentication authentication){
        String name=authentication.getName();
        
        List<String> roles=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Auth user =  urepo.findByUsername(name);
               // .orElseThrow(() -> new UsernameNotFoundException("User not found"))

            int userId =user.getId();
        Date currDate=new Date();
        Date expiray=new Date(currDate.getTime()+expiration);

        return Jwts.builder()
                .setSubject(name)
                .claim("roles",roles)
                .claim("userId",userId)
                .setIssuedAt(currDate)
                .setExpiration(expiray)
                .signWith(key())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

	

}




