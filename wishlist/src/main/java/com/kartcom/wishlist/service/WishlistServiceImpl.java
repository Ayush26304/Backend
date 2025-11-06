package com.kartcom.wishlist.service;

import com.kartcom.wishlist.dto.*;
import com.kartcom.wishlist.entity.WishlistItem;
import com.kartcom.wishlist.exception.WishlistException;
import com.kartcom.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private  WishlistRepository wishlistRepository;

    public WishlistResponse addToWishlist(int userId, WishlistRequest request) {
        wishlistRepository.findByUserIdAndProductId(userId, request.getProductId())
                .ifPresent(item -> {
                    throw new WishlistException("Product already in wishlist.");
                });

        WishlistItem item = new WishlistItem();
        item.setUserId(userId);
        item.setProductId(request.getProductId());
        item.setAddedAt(LocalDateTime.now());

        WishlistItem saved = wishlistRepository.save(item);
        return new WishlistResponse(saved.getId(), saved.getProductId(), saved.getAddedAt());
    }

    public List<WishlistResponse> getWishlist(int userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(item -> new WishlistResponse(item.getId(), item.getProductId(), item.getAddedAt()))
                .collect(Collectors.toList());
    }

    public void removeFromWishlist(int userId, Long productId) {
        WishlistItem item = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new WishlistException("Product not found in wishlist."));
        wishlistRepository.delete(item);
    }
}
