package com.kartcom.catalog.service;

import java.util.List;

import com.kartcom.catalog.dto.ProductDto;
import com.kartcom.catalog.entity.Product;

public interface ProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getProductsByCategory(Long categoryId);
    ProductDto getProductById(Long id);
    Product addProduct(ProductDto dto);
    Product updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long id);
    void reduceStock(Long productId, Integer quantity);
}

