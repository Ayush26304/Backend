package com.kartcom.order.service;

import java.util.List;

import com.kartcom.order.dto.OrderRequestDto;
import com.kartcom.order.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(int userId, String token, OrderRequestDto request);
    OrderResponseDto getOrderById(Long orderId);
    List<OrderResponseDto> getUserOrders(int userId);
    byte[] downloadInvoice(Long orderId);
	void deleteOrder(Long orderId);
	void updateOrderStatus(Long orderId, String status);
	OrderResponseDto getOrderByInvoice(String invoiceNumber);
	List<OrderResponseDto> getAllOrders();
}
