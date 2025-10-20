package com.cts.Order_Service.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
    private String category;
}
