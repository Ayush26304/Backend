package com.kartcom.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartcom.catalog.dto.ProductDto;
import com.kartcom.catalog.entity.Category;
import com.kartcom.catalog.entity.Product;
import com.kartcom.catalog.exception.ResourceNotFoundException;
import com.kartcom.catalog.exception.StockUnavailableException;
import com.kartcom.catalog.repository.CategoryRepository;
import com.kartcom.catalog.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> dtos = new ArrayList<>();

        for (Product product : products) {
            ProductDto dto = new ProductDto();
            BeanUtils.copyProperties(product, dto);
            dto.setCategoryId(product.getCategory().getId());
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<ProductDto> dtos = new ArrayList<>();

        for (Product product : products) {
            ProductDto dto = new ProductDto();
            BeanUtils.copyProperties(product, dto);
            dto.setCategoryId(categoryId);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }

        Product product = optional.get();
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        dto.setCategoryId(product.getCategory().getId());

        return dto;
    }

    @Override
    public Product addProduct(ProductDto dto) {
        Optional<Category> optionalCategory = categoryRepository.findById(dto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId());
        }

        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setCategory(optionalCategory.get());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDto dto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(dto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId());
        }

        Product product = optionalProduct.get();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(optionalCategory.get());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

	@Override
	public void reduceStock(Long productId, Integer quantity) {
		Product product =  productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

	        if (product.getStock() < quantity) {
	            throw new StockUnavailableException("Insufficient stock for product: " + product.getName());
	        }

	        product.setStock(product.getStock() - quantity);
	        productRepository.save(product);
		
	}
	@Override
	public List<ProductDto> searchProducts(String keyword) {
	    List<Product> products;

	    if (keyword == null || keyword.trim().isEmpty()) {
	        products = productRepository.findAll();
	    } else {
	        products = productRepository.searchProduct(keyword);
	    }

	    List<ProductDto> dtos = new ArrayList<>();
	    for (Product product : products) {
	        ProductDto dto = new ProductDto();
	        BeanUtils.copyProperties(product, dto);
	        dto.setCategoryId(product.getCategory().getId());
	        dtos.add(dto);
	    }

	    return dtos;
	}
	public List<ProductDto> getLatestProducts() {
	    List<Product> latestProducts = productRepository.findTop10ByOrderByIdDesc();
	    List<ProductDto> dtos = new ArrayList<>();

	    for (Product product : latestProducts) {
	        ProductDto dto = new ProductDto();
	        dto.setId(product.getId());
	        dto.setName(product.getName());
	        dto.setDescription(product.getDescription());
	        dto.setPrice(product.getPrice());
	        dto.setStock(product.getStock());
	        dto.setImageUrl(product.getImageUrl());
	        dto.setCategoryId(product.getCategory().getId()); // assuming category is an entity
	        dtos.add(dto);
	    }

	    return dtos;
	}
}
