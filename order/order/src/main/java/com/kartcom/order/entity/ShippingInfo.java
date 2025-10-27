package com.kartcom.order.entity;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ShippingInfo {
    private String address;
    private String city;
    private String postalCode;
    private String phone;
}
