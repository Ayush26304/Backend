package com.kartcom.order.dto;

import com.kartcom.order.entity.ShippingInfo;
 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private ShippingInfo shipping;
}
