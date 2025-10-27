package com.kartcom.catalog.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
@Component
public class JwtUtil {

   @Value("${app.security.secretKey}")
   private String secretKey;

   private SecretKey key(){
       return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
   }

   private Claims extractClaims(String token){
       return Jwts.parserBuilder()
               .setSigningKey(key())
               .build()
               .parseClaimsJws(token)
               .getBody();
   }
   public String extractName(String token){
       return extractClaims(token).getSubject();
   }
   public List<String> extractRole(String token){
       return extractClaims(token).get("roles",List.class);
   }
}