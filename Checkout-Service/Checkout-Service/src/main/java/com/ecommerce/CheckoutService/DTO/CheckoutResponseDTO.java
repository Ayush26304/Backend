package com.ecommerce.CheckoutService.DTO;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponseDTO {
    private List<ProductSummaryDTO> items;
    private BigDecimal totalAmount;
    private List<String> availablePaymentMethods;
}

