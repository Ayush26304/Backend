package com.kartcom.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kartcom.order.config.CatalogFeignConfig;
import com.kartcom.order.dto.ProductDto;

@FeignClient(name = "catalog",configuration = CatalogFeignConfig.class)
public interface ProductFeignClient {
    @GetMapping("/api/catalog/product/{id}")
    ProductDto getProductById(@PathVariable Long id);

    @PutMapping("/api/catalog/product/reduce-stock/{id}")
    void reduceStock(@PathVariable Long id, @RequestParam Integer quantity);
}
