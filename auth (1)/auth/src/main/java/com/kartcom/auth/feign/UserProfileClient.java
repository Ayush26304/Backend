package com.kartcom.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kartcom.auth.dto.MinimalProfileDto;
import com.kartcom.auth.interceptor.FeignClientInterceptor;

//Feign client in Auth Service
@FeignClient(name = "gatedemo",configuration=FeignClientInterceptor.class)
public interface UserProfileClient {
 @PostMapping("/api/user/create-minimal")
 ResponseEntity<String> createMinimalProfile(@RequestBody MinimalProfileDto dto);
}
