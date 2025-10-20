package com.ecommerce.Wishlist_Service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCollectionDTO {

    @NotBlank(message = "Collection name is required")
    @Size(max = 255, message = "Collection name must not exceed 255 characters")
    private String name;

    @NotNull(message = "User ID is required")
    private Long userId;
}