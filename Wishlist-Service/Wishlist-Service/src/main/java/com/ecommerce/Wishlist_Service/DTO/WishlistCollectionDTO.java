package com.ecommerce.Wishlist_Service.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistCollectionDTO {

    private Long id;

    @NotBlank(message = "Collection name is required")
    @Size(max = 255, message = "Collection name must not exceed 255 characters")
    private String name;

    private Long userId;

    @Builder.Default //Ensures default values (like new ArrayList<>) are preserved when using the builder
    private List<WishlistItemDTO> items = new ArrayList<>();

    @JsonProperty("createdAt") //converting the object to JSON, appear as "createdAt"
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
