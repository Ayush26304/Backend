package com.ecommerce.Cart_Service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.Cart_Service.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUserId(Long userId);
	
    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

}
