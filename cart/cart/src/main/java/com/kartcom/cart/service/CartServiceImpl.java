package com.kartcom.cart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartcom.cart.dto.CartItemRequestDto;
import com.kartcom.cart.dto.CartItemResponseDto;
import com.kartcom.cart.dto.ProductDto;
import com.kartcom.cart.entity.CartItem;
import com.kartcom.cart.exception.ResourceNotFoundException;
import com.kartcom.cart.feign.CatalogFeignClient;
import com.kartcom.cart.repository.CartItemRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private CatalogFeignClient catalogClient;

    @Override
    public List<CartItemResponseDto> getCartItems(int userId) {
        List<CartItem> items = cartRepo.findByUserId(userId);
        List<CartItemResponseDto> response = new ArrayList<>();

        for (CartItem item : items) {
            ProductDto product = catalogClient.getProductById(item.getProductId());

            CartItemResponseDto dto = new CartItemResponseDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setImageUrl(product.getImageUrl());
            dto.setPrice(product.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setAvailableStock(product.getStock());

            response.add(dto);
        }

        return response;
    }

    @Override
    public void addToCart(int userId, CartItemRequestDto dto) {
        ProductDto product = catalogClient.getProductById(dto.getProductId());

        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        Optional<CartItem> existing = cartRepo.findByUserIdAndProductId(userId, dto.getProductId());

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + dto.getQuantity());
            cartRepo.save(item);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            item.setAddedAt(LocalDateTime.now());
            cartRepo.save(item);
        }
    }
    @Transactional
    @Override
    public void updateCartItem(int userId, CartItemRequestDto dto) {
        Optional<CartItem> optional = cartRepo.findByUserIdAndProductId(userId, dto.getProductId());

        if (!optional.isPresent()) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        ProductDto product = catalogClient.getProductById(dto.getProductId());

        if (dto.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds stock");
        }

        CartItem item = optional.get();
        item.setQuantity(dto.getQuantity());
        cartRepo.save(item);
    }
    @Transactional
    @Override
    public void removeCartItem(int userId, Long productId) {
        cartRepo.deleteByUserIdAndProductId(userId, productId);
    }
    @Transactional
    @Override
    public void clearCart(int userId) {
        cartRepo.deleteByUserId(userId);
    }
}

