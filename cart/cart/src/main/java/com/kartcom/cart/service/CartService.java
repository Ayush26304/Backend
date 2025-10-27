package com.kartcom.cart.service;

import java.util.List;

import com.kartcom.cart.dto.CartItemRequestDto;
import com.kartcom.cart.dto.CartItemResponseDto;

public interface CartService {
    List<CartItemResponseDto> getCartItems(int userId);
    void addToCart( int userId, CartItemRequestDto dto);
    void updateCartItem(int userId, CartItemRequestDto dto);
    void removeCartItem(int userId, Long productId);
    void clearCart(int userId);
}