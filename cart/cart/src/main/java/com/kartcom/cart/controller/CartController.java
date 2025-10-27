package com.kartcom.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartcom.cart.dto.CartItemRequestDto;
import com.kartcom.cart.dto.CartItemResponseDto;
import com.kartcom.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> getCart(Authentication auth) {
        int userId = Integer.parseInt(auth.getName());
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(Authentication auth, @RequestBody CartItemRequestDto dto) {
        int userId = Integer.parseInt(auth.getName());
        cartService.addToCart(userId, dto);
        return ResponseEntity.ok("Item added to cart");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCart(Authentication auth, @RequestBody CartItemRequestDto dto) {
        int userId = Integer.parseInt(auth.getName());
        cartService.updateCartItem(userId, dto);
        return ResponseEntity.ok("Cart item updated");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(Authentication auth, @PathVariable Long productId) {
        int userId = Integer.parseInt(auth.getName());
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication auth) {
        int userId = Integer.parseInt(auth.getName());
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}
