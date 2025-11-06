package com.kartcom.wishlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kartcom.wishlist.entity.WishlistItem;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserId(int userId);
    Optional<WishlistItem> findByUserIdAndProductId(int userId, Long productId);
    void deleteByUserIdAndProductId(int userId, Long productId);
}
