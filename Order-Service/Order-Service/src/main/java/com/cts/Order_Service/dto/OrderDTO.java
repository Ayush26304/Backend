package com.cts.Order_Service.dto;



import com.cts.Order_Service.Entity.OrderStatus;
import com.cts.Order_Service.Entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private String orderNumber;
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
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String transactionId;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private List<OrderItemDTO> orderItems;
    private int totalItems;
}