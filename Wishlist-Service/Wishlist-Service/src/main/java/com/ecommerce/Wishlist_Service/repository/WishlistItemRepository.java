package com.ecommerce.Wishlist_Service.repository;

import com.ecommerce.Wishlist_Service.entity.WishlistItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    @Query("SELECT i FROM WishlistItem i WHERE i.collection.userId = :userId AND i.productId = :productId")
    List<WishlistItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT i FROM WishlistItem i WHERE i.collection.userId = :userId")
    List<WishlistItem> findByUserId(@Param("userId") Long userId);

    boolean existsByCollectionIdAndProductId(Long collectionId, Long productId);
}