package com.ecommerce.Wishlist_Service.controller;

import com.ecommerce.Wishlist_Service.DTO.*;
import com.ecommerce.Wishlist_Service.service.WishlistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // Get all collections for a user
    @GetMapping("/user/{userId}/collections")
    public ResponseEntity<List<WishlistCollectionDTO>> getUserCollections(@PathVariable Long userId) {
        List<WishlistCollectionDTO> collections = wishlistService.getUserCollections(userId);
        return ResponseEntity.ok(collections);
    }

    // Create a new collection
    
    @PostMapping("/collections")
    public ResponseEntity<WishlistCollectionDTO> createCollection(@Valid @RequestBody
    		CreateCollectionDTO createCollectionDTO) ////@RequestBody to bind incoming JSON to Java objects,
	//@Valid to ensure the data meets business rules before processing
    {
    	
        WishlistCollectionDTO collection = wishlistService.createCollection(createCollectionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(collection);
    }

    // Get a specific collection
    @GetMapping("/collections/{collectionId}/user/{userId}")
    public ResponseEntity<WishlistCollectionDTO> getCollection(
            @PathVariable Long collectionId,
            @PathVariable Long userId) {
        WishlistCollectionDTO collection = wishlistService.getCollection(collectionId, userId);
        return collection != null ? ResponseEntity.ok(collection) : ResponseEntity.notFound().build();
    }

    // Update collection name
    @PutMapping("/collections/{collectionId}/user/{userId}")
    public ResponseEntity<WishlistCollectionDTO> updateCollection(
            @PathVariable Long collectionId,
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        String newName = request.get("name");
        WishlistCollectionDTO updatedCollection = wishlistService
        		.updateCollection(collectionId, userId, newName);
        return updatedCollection != null ? ResponseEntity
        		.ok(updatedCollection) : ResponseEntity.notFound().build();
    }

    // Delete a collection
    @DeleteMapping("/collections/{collectionId}/user/{userId}")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable Long collectionId,
            @PathVariable Long userId) {
        wishlistService.deleteCollection(collectionId, userId);
        return ResponseEntity.noContent().build();
    }

    // Add item to collection
    @PostMapping("/collections/{collectionId}/user/{userId}/items")
    public ResponseEntity<WishlistItemDTO> addItemToCollection(
            @PathVariable Long collectionId,
            @PathVariable Long userId,
            @Valid @RequestBody AddItemsToCollectionDTO addItemDTO) {
        WishlistItemDTO item = wishlistService.addItemToCollection(collectionId, userId, addItemDTO);
        return item != null ? ResponseEntity
        		.status(HttpStatus.CREATED).body(item) : ResponseEntity.badRequest().build();
    }

    // Remove item from collection
    @DeleteMapping("/collections/{collectionId}/user/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long userId,
            @PathVariable Long itemId) {
        wishlistService.removeItemFromCollection(collectionId, userId, itemId);
        return ResponseEntity.noContent().build();
    }

    // Get all items for a user across all collections
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<List<WishlistItemDTO>> getAllUserItems(@PathVariable Long userId) {
        List<WishlistItemDTO> items = wishlistService.getAllUserItems(userId);
        return ResponseEntity.ok(items);
    }

    // Check if a product is in user's wishlist
    @GetMapping("/user/{userId}/product/{productId}/exists")
    public ResponseEntity<Map<String, Boolean>> isProductInWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        boolean exists = wishlistService.isProductInWishlist(userId, productId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
