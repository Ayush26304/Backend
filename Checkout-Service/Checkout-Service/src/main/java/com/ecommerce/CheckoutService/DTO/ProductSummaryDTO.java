package com.ecommerce.CheckoutService.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDTO {
    private int productId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
}
