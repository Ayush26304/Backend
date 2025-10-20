package com.cts.Order_Service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateOrderDTO {
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
}