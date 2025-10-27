package com.kartcom.User.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth")
public interface AuthFeignClient {
    @GetMapping("/api/validate")
    ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token);
}

