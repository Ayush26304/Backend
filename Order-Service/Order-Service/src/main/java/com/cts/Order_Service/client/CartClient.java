package com.cts.Order_Service.client;

import com.cts.Order_Service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cart-service", url = "http://localhost:8082") // Replace with actual service name or URL
public interface CartClient {

    @GetMapping("/api/cart/user/{userId}")
    CartDTO getCartByUserId(@PathVariable("userId") Long userId);

    @DeleteMapping("/api/cart/user/{userId}")
    void clearCart(@PathVariable("userId") Long userId);
}