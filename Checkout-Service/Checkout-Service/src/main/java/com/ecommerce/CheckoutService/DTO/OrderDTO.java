package com.ecommerce.CheckoutService.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime estimatedDeliveryDate;
    
    private List<OrderItemDto> items;
}