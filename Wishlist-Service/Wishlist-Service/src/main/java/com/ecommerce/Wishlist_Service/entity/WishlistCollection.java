package com.ecommerce.Wishlist_Service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlist_collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Collection name is required")
    @Size(max = 255, message = "Collection name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<WishlistItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // addItems
    public void addItem(WishlistItem item) {
        items.add(item);
        item.setCollection(this);
    }

    //removeItems
    public void removeItem(WishlistItem item) {
        items.remove(item);
        item.setCollection(null);
    }

	
}
