package com.ecommerce.Cart_Service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.Cart_Service.entity.Cart;
import com.ecommerce.Cart_Service.entity.CartItems;
import com.ecommerce.Cart_Service.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	public Cart getOrCreateCart(Long userId) {
	    return cartRepository.findByUserId(userId)
	            .orElseGet(() -> {
	                Cart cart = new Cart();
	                cart.setUserId(userId);
	                return cartRepository.save(cart);
	            });
	}

//AddItems
    @Transactional
    public void addItem(Long userId, CartItems item) {
    	
    	Cart cart = getOrCreateCart( userId);
    	item.setCart(cart);
    	cart.getItems().add(item);
    	cartRepository.save(cart);
    }

//Remove
    @Transactional
    public void removeItem(Long userId, Long itemId) {
    	
        Cart cart = getOrCreateCart(userId);
        
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }
    
//Clear
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

//TotalPrice    
    @Transactional
    public double getTotalPrice(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

//TotalQuantity
    @Transactional
    public int getTotalQuantity(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().stream().mapToInt(item -> item.getQuantity()).sum();
    }

	
}
