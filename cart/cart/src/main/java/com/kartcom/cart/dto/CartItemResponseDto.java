package com.kartcom.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
    private Long productId;
    private String name;
    private String imageUrl;
    private Double price;
    private Integer quantity;
    private Integer availableStock;
}

