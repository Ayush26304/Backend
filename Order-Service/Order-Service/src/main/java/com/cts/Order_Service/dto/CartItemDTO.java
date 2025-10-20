package com.cts.Order_Service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private Long productId;
    private int quantity;
}