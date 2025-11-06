package com.kartcom.order.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.kartcom.order.config.CatalogFeignConfig;
import com.kartcom.order.dto.CartItemResponseDto;
import com.kartcom.order.dto.ProductDto;

@FeignClient(name = "gatedemo")
public interface CartFeignClient {
    @GetMapping("/api/cart")
    List<CartItemResponseDto> getCartItems(@RequestHeader("Authorization") String token);

    @DeleteMapping("/api/cart/clear")
    void clearCart(@RequestHeader("Authorization") String token);
    @GetMapping("/api/catalog/product/{id}")
    ProductDto getProductById(@PathVariable Long id);

    @PutMapping("/api/catalog/product/reduce-stock/{id}")
    void reduceStock(@PathVariable Long id, @RequestParam Integer quantity);
}
