package com.kartcom.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kartcom.order.dto.CartItemResponseDto;

@FeignClient(name = "cart")
public interface CartFeignClient {
    @GetMapping("/api/cart")
    List<CartItemResponseDto> getCartItems(@RequestHeader("Authorization") String token);

    @DeleteMapping("/api/cart/clear")
    void clearCart(@RequestHeader("Authorization") String token);
}
