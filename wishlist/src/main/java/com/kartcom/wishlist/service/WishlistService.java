package com.kartcom.wishlist.service;

import com.kartcom.wishlist.dto.WishlistRequest;
import com.kartcom.wishlist.dto.WishlistResponse;

import java.util.List;

public interface WishlistService {

   
    WishlistResponse addToWishlist(int userId, WishlistRequest request);

    
    List<WishlistResponse> getWishlist(int userId);

    
    void removeFromWishlist(int userId, Long productId);
}
