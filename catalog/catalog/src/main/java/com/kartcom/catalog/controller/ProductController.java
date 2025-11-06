package com.kartcom.catalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.kartcom.catalog.dto.ProductDto;

import com.kartcom.catalog.entity.Product;

import com.kartcom.catalog.service.ProductService;



@RestController
@RequestMapping("/api/catalog/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(service.getAllProducts());
    }
         @GetMapping("/latest") 
         public ResponseEntity<List<ProductDto>> getLatestProducts() {
    	return ResponseEntity.ok(service.getLatestProducts()); 
    	}

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.getProductsByCategory(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id ) {
    
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> add(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(service.addProduct(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam(required = false) String keyword) {
        List<ProductDto> results = service.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }
    
    @PutMapping("/reduce-stock/{id}")
    public ResponseEntity<String> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
    	service.reduceStock(id, quantity);
        return ResponseEntity.ok("Stock reduced successfully");
    }
}