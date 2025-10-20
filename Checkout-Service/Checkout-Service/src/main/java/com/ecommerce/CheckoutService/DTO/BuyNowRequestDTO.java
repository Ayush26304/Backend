package com.ecommerce.CheckoutService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowRequestDTO {
    private Long userId;
    private Long productId;
    private int quantity;
    private String shippingAddress;
    private String paymentMethod;
}
