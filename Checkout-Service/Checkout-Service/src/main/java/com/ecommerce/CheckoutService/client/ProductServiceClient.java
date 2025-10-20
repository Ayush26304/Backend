package com.ecommerce.CheckoutService.client;

import com.ecommerce.CheckoutService.DTO.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", url = "http://") 
public interface ProductServiceClient {

    @GetMapping("/products/{productId}")
    ProductDTO getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/products/{productId}/reserve")
    boolean reserveProductStock(
        @PathVariable("productId") Long productId,
        @RequestParam("quantity") int quantity
    );
}
