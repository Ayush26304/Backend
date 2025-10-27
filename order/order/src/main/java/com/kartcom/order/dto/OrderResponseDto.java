package com.kartcom.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kartcom.order.entity.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String invoiceNumber;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
    private ShippingInfo shipping;
}
