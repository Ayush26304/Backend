package com.kartcom.order.service;
 
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.kartcom.order.dto.CartItemResponseDto;
import com.kartcom.order.dto.OrderItemDto;
import com.kartcom.order.dto.OrderRequestDto;
import com.kartcom.order.dto.OrderResponseDto;
import com.kartcom.order.dto.ProductDto;
import com.kartcom.order.entity.Order;
import com.kartcom.order.entity.OrderItem;
import com.kartcom.order.exception.PaymentFailedException;
import com.kartcom.order.exception.ResourceNotFoundException;
import com.kartcom.order.exception.StockUnavailableException;
import com.kartcom.order.feign.CartFeignClient;
import com.kartcom.order.feign.ProductFeignClient;
import com.kartcom.order.repositoy.OrderRepository;
 
import jakarta.transaction.Transactional;
@Service
public class OrderServiceImpl implements OrderService {
 
    @Autowired private OrderRepository orderRepo;
    @Autowired private CartFeignClient cartClient;
    @Autowired private ProductFeignClient productClient;
 
    @Override
    @Transactional
    public OrderResponseDto placeOrder(int userId, String token, OrderRequestDto request) {
        List<CartItemResponseDto> cartItems = cartClient.getCartItems(token);
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }
 
        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
 
        for (CartItemResponseDto cartItem : cartItems) {
            ProductDto product = productClient.getProductById(cartItem.getProductId());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new StockUnavailableException("Insufficient stock for " + product.getName());
            }
 
            productClient.reduceStock(product.getId(), cartItem.getQuantity());
 
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(cartItem.getQuantity());
            orderItems.add(item);
 
            total += product.getPrice() * cartItem.getQuantity();
        }
 
        boolean paymentSuccess = true;
        if (!paymentSuccess) {
            throw new PaymentFailedException("Payment failed");
        }
 
        Order order = new Order();
        order.setUserId(userId);
        order.setInvoiceNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setTotalAmount(total);
        order.setStatus("PAID");
        order.setOrderDate(LocalDateTime.now());
        order.setShipping(request.getShipping());
        order.setItems(orderItems);
        orderItems.forEach(i -> i.setOrder(order));
 
        orderRepo.save(order);
        cartClient.clearCart(token);
 
        return mapToResponse(order);
    }
 
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return mapToResponse(order);
    }
 
    @Override
    public List<OrderResponseDto> getUserOrders(int userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
 
    @Override
    public byte[] downloadInvoice(Long orderId) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
 
        String invoiceText = "Invoice #" + order.getInvoiceNumber() + "\n"
            + "Order ID: " + order.getId() + "\n"
            + "User ID: " + order.getUserId() + "\n"
            + "Total: ₹" + order.getTotalAmount() + "\n"
            + "Status: " + order.getStatus() + "\n"
            + "Date: " + order.getOrderDate() + "\n";
 
        return invoiceText.getBytes(StandardCharsets.UTF_8);
    }
 
    @Override
    public OrderResponseDto getOrderByInvoice(String invoiceNumber) {
        Order order = orderRepo.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with invoice: " + invoiceNumber));
        return mapToResponse(order);
    }
 
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(status);
        orderRepo.save(order);
    }
 
    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
 
    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        orderRepo.delete(order);
    }
 
    private OrderResponseDto mapToResponse(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDto dto = new OrderItemDto();
            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());
 
        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(order.getId());
        response.setInvoiceNumber(order.getInvoiceNumber());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setItems(itemDtos);
        response.setShipping(order.getShipping());
 
        return response;
    }
}
 

 
 