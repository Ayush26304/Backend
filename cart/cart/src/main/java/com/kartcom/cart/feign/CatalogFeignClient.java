package com.kartcom.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kartcom.cart.dto.ProductDto;

@FeignClient(name = "gatedemo",path="/")
public interface CatalogFeignClient {

    @GetMapping("/api/catalog/product/{id}")
    ProductDto getProductById(@PathVariable("id") Long productId);
}

