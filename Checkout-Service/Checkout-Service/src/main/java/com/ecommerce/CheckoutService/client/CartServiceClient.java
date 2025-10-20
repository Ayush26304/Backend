package com.ecommerce.CheckoutService.client;

import com.ecommerce.CheckoutService.DTO.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", url = "http://localhost:") 
public interface CartServiceClient {

    @GetMapping("/cart/{userId}")
    CartDTO getCartByUserId(@PathVariable("userId") Long userId);
}