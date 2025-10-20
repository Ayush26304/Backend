package com.ecommerce.Wishlist_Service.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Column(name = "category")
    private String category;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private WishlistCollection collection;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
    }
}