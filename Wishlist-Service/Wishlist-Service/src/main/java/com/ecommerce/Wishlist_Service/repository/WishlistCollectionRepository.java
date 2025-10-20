package com.ecommerce.Wishlist_Service.repository;

import com.ecommerce.Wishlist_Service.entity.WishlistCollection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistCollectionRepository extends JpaRepository<WishlistCollection, Long> {

    @Query("SELECT c FROM WishlistCollection c LEFT JOIN FETCH c.items WHERE c.userId = :userId")
    List<WishlistCollection> findByUserIdWithItems(@Param("userId") Long userId);

    List<WishlistCollection> findByUserId(Long userId);

    Optional<WishlistCollection> findByIdAndUserId(Long collectionId, Long userId);
}