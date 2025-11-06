package com.kartcom.wishlist.controller;

import com.kartcom.wishlist.dto.*;
import com.kartcom.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")

@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
     
    @PostMapping("/add")
    public ResponseEntity<WishlistResponse> addToWishlist(
            Authentication authentication,
            @RequestBody WishlistRequest request) {

        int userId = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(wishlistService.addToWishlist(userId, request));
    }

    @GetMapping("/get")
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication authentication) {
        int userId = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @DeleteMapping("/{productId}") 
    public ResponseEntity<Void> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long productId) {

        int userId = Integer.parseInt(authentication.getName());
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
