package com.ecommerce.CheckoutService.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.ecommerce.CheckoutService.DTO.CreateOrderDTO;
import com.ecommerce.CheckoutService.DTO.OrderDTO;

@FeignClient(name = "order-service", url = "http://localhost:8083")
public interface OrderServiceClient {

    @PostMapping("/orders")
    OrderDTO createOrder(@RequestBody CreateOrderDTO orderDTO);
}