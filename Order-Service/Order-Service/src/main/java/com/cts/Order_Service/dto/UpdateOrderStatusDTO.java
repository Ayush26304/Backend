package com.cts.Order_Service.dto;

import com.cts.Order_Service.Entity.OrderStatus;
import com.cts.Order_Service.Entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateOrderStatusDTO {
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String notes;
}