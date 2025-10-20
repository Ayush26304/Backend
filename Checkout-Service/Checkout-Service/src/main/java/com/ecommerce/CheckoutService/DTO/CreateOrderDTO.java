package com.ecommerce.CheckoutService.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private Long userId;
    private String shippingAddress;
    private String billingAddress;
    private String phoneNumber;
    private String email;
    private String notes;
    private String paymentMethod;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private String couponCode;
    private List<OrderItemDto> items;
}
