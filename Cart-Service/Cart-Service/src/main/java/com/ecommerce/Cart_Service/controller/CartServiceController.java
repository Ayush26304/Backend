package com.ecommerce.Cart_Service.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.ecommerce.Cart_Service.entity.Cart;
import com.ecommerce.Cart_Service.entity.CartItems;
import com.ecommerce.Cart_Service.service.CartService;
 
@RestController
@RequestMapping("/cart")
public class CartServiceController {
 
	@Autowired
	public CartService cartService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<Cart> getCart(@PathVariable Long userId){
		Cart cart=cartService.getOrCreateCart(userId);
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/{userId}/items")
	public ResponseEntity<Cart> addCart(@PathVariable Long userId, @RequestBody CartItems item){
		cartService.addItem(userId, item);
		Cart updatedCart=cartService.getOrCreateCart(userId);
		return ResponseEntity.ok(updatedCart);
	}
	
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<String> removeItem(@PathVariable Long userId, @PathVariable Long itemId){
		cartService.removeItem(userId,itemId);
		return ResponseEntity.ok("Item remove from cart");
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> clearCart(@PathVariable Long userId, @PathVariable Long itemId){
		cartService.clearCart(itemId);
		return ResponseEntity.ok("Cart cleared");
	}
	
	@GetMapping("/{userId}/total-price")
	public ResponseEntity<Double> gettotalPrice(@PathVariable Long userId){
		double totalPrice=cartService.getTotalPrice(userId);
		return ResponseEntity.ok(totalPrice);
	}
	
	@GetMapping("/{userId}/total-quantity")
	public ResponseEntity<Integer> gettotalQuantity(@PathVariable Long userId){
		int totalQuantity=cartService.getTotalQuantity(userId);
		return ResponseEntity.ok(totalQuantity);
	}
	
}
 
 