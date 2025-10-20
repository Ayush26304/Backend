package com.cts.Order_Service.client;

import com.cts.Order_Service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/products/{productId}")
    ProductDTO getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/products/{productId}/reserve")
    boolean reserveProductStock(@PathVariable("productId") Long productId,
                                @RequestParam("quantity") int quantity);

    @PostMapping("/products/{productId}/release")
    void releaseProductStock(@PathVariable("productId") Long productId,
                             @RequestParam("quantity") int quantity);

    @GetMapping("/products/{productId}/availability")
    boolean isProductAvailable(@PathVariable("productId") Long productId,
                               @RequestParam("quantity") int quantity);
}