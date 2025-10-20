package com.ecommerce.Wishlist_Service.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemsToCollectionDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}