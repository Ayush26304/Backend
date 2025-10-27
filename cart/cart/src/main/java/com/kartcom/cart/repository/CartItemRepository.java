package com.kartcom.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kartcom.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(int userId);

    Optional<CartItem> findByUserIdAndProductId(int userId, Long productId);

    boolean existsByUserId(int userId);

    void deleteByUserId(int userId);

    void deleteByUserIdAndProductId(int userId, Long productId);
}

