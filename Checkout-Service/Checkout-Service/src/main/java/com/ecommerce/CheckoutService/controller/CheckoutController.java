package com.ecommerce.CheckoutService.controller;

import com.ecommerce.CheckoutService.DTO.*;
import com.ecommerce.CheckoutService.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    // Buy Now checkout
    @PostMapping("/buy-now")
    public OrderDTO processBuyNow(@RequestBody BuyNowRequestDTO request) {
        return checkoutService.processBuyNow(request);
    }

   
     // Cart checkout
    @PostMapping("/cart")
    public CheckoutResponseDTO checkoutCart(@RequestBody CartCheckoutRequestDTO request) {
        return checkoutService.checkoutCart(request);
    }
}