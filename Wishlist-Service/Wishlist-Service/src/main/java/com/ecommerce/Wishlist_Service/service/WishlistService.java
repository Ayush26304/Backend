package com.ecommerce.Wishlist_Service.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ecommerce.Wishlist_Service.DTO.AddItemsToCollectionDTO;
import com.ecommerce.Wishlist_Service.DTO.CreateCollectionDTO;
import com.ecommerce.Wishlist_Service.DTO.WishlistCollectionDTO;
import com.ecommerce.Wishlist_Service.DTO.WishlistItemDTO;
import com.ecommerce.Wishlist_Service.entity.WishlistCollection;
import com.ecommerce.Wishlist_Service.entity.WishlistItem;
import com.ecommerce.Wishlist_Service.repository.WishlistCollectionRepository;
import com.ecommerce.Wishlist_Service.repository.WishlistItemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistCollectionRepository collectionRepository;
    private final WishlistItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<WishlistCollectionDTO> getUserCollections(Long userId) {
    	
        List<WishlistCollection> collections = collectionRepository.findByUserIdWithItems(userId);
        
        return collections.stream()
                .map(collection -> convertToCollectionDTO(collection))
                .collect(Collectors.toList());
    }

    //creating new WishList collection for user
    public WishlistCollectionDTO createCollection(CreateCollectionDTO createCollectionDTO) {
    	
        WishlistCollection collection = WishlistCollection.builder()
                .name(createCollectionDTO.getName())
               .userId(createCollectionDTO.getUserId())
              .createdAt(LocalDateTime.now())
             .updatedAt(LocalDateTime.now())
           .build();

        WishlistCollection savedCollection = collectionRepository.save(collection);
        
        
        return convertToCollectionDTO(savedCollection);
    }

    
    //Add item to collection
    public WishlistItemDTO addItemToCollection(Long collectionId, Long userId,
    		AddItemsToCollectionDTO addItemDTO) {
        WishlistCollection collection = collectionRepository.findByIdAndUserId(collectionId, userId)
        		.orElse(null);

        if (collection == null) {
            return null;
        }

        boolean exists = itemRepository.existsByCollectionIdAndProductId(collectionId,
        		addItemDTO.getProductId());
        
        if (exists) {
            return null;
        }

        WishlistItem item = WishlistItem.builder()
                    .productId(addItemDTO.getProductId())
                 .productName(addItemDTO.getProductName())
              .price(addItemDTO.getPrice())
                   .category(addItemDTO.getCategory())
               .imageUrl(addItemDTO.getImageUrl())
                  .collection(collection)
                .build();

        collection.addItem(item);
        WishlistItem savedItem = itemRepository.save(item);

        return convertToItemDTO(savedItem);
    }
    
    //specific collection
@Transactional(readOnly = true)
public WishlistCollectionDTO getCollection(Long collectionId, Long userId) {
    WishlistCollection collection = collectionRepository.findByIdAndUserId(collectionId, userId).orElse(null);

    if (collection == null) {
        return null; // or return an empty DTO or log the issue
    }

    return convertToCollectionDTO(collection);
}

    
    
    
    //remove items from the collection
    public void removeItemFromCollection(Long collectionId, Long userId, Long itemId) {
        WishlistCollection collection = collectionRepository.findByIdAndUserId(collectionId, userId).orElse(null);

        if (collection == null) {
            return; // Collection not found, exit silently or log if needed
        }

        WishlistItem item = itemRepository.findById(itemId).orElse(null);

        if (item == null || !item.getCollection().getId().equals(collectionId)) {
            return; // Item not found or doesn't belong to this collection
        }

        collection.removeItem(item);
        itemRepository.delete(item);
    }
    
    //delete collection
    public void deleteCollection(Long collectionId, Long userId) {
        WishlistCollection collection = collectionRepository.findByIdAndUserId(collectionId, userId).orElse(null);

        if (collection == null) {
            return; // or log the issue
        }

        collectionRepository.delete(collection);
    }
    
    
    //Get all items for a user across all collections
     @Transactional(readOnly = true)
    public List<WishlistItemDTO> getAllUserItems(Long userId) {
        List<WishlistItem> items = itemRepository.findByUserId(userId);
        return items.stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList());
    }
    
     //specific product from WishList
     @Transactional(readOnly = true)
     public boolean isProductInWishlist(Long userId, Long productId) {
         List<WishlistItem> items = itemRepository.findByUserIdAndProductId(userId, productId);
         return !items.isEmpty();
     }
    
    
     
     public WishlistCollectionDTO updateCollection(Long collectionId, Long userId, String newName) {
    	    WishlistCollection collection = collectionRepository
    	    		.findByIdAndUserId(collectionId, userId).orElse(null);

    	    if (collection == null) {
    	        return null; // or throw an exception if preferred
    	    }

    	    collection.setName(newName);
    	     collection.setUpdatedAt(LocalDateTime.now());

    	    WishlistCollection updatedCollection = collectionRepository.save(collection);
    	    
    	    return convertToCollectionDTO(updatedCollection);
    	}
     
     
     
    private WishlistCollectionDTO convertToCollectionDTO(WishlistCollection collection) {
        return WishlistCollectionDTO.builder()
                .id(collection.getId())
                  .name(collection.getName())
                     .userId(collection.getUserId())
                   .createdAt(collection.getCreatedAt())
               .updatedAt(collection.getUpdatedAt())
                .build();
    }
    
    private WishlistItemDTO convertToItemDTO(WishlistItem item) {
        return WishlistItemDTO.builder()
              .id(item.getId())
                .productId(item.getProductId())
                  .productName(item.getProductName())
               .price(item.getPrice())
                .category(item.getCategory())
                  .imageUrl(item.getImageUrl())
                 .addedAt(item.getAddedAt())
                .build();
    }
    
}