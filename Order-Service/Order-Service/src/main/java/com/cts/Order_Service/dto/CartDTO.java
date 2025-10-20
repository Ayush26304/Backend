package com.cts.Order_Service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private List<CartItemDTO> cartItems;
}