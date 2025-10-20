package com.cts.Order_Service.Service;

import com.cts.Order_Service.client.ProductServiceClient;
import com.cts.Order_Service.dto.*;
import com.cts.Order_Service.dto.CartDTO;
import com.cts.Order_Service.dto.UpdateOrderStatusDTO;
import com.cts.Order_Service.Entity.*;
import com.cts.Order_Service.Entity.OrderStatus;
import com.cts.Order_Service.Entity.PaymentStatus;
import com.cts.Order_Service.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;

    @Transactional
    public OrderDTO createOrderFromCart(Long userId, CreateOrderDTO createOrderDTO, CartDTO cartDTO) {
        log.info("Creating order from cart for user: {}", userId);

        if (cartDTO.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        Order order = Order.builder()
                .userId(userId)
                .shippingAddress(createOrderDTO.getShippingAddress())
                .billingAddress(createOrderDTO.getBillingAddress() != null ? createOrderDTO.getBillingAddress() : createOrderDTO.getShippingAddress())
                .phoneNumber(createOrderDTO.getPhoneNumber())
                .email(createOrderDTO.getEmail())
                .notes(createOrderDTO.getNotes())
                .paymentMethod(createOrderDTO.getPaymentMethod())
                .taxAmount(createOrderDTO.getTaxAmount())
                .shippingAmount(createOrderDTO.getShippingAmount())
                .discountAmount(createOrderDTO.getDiscountAmount())
                .couponCode(createOrderDTO.getCouponCode())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .estimatedDeliveryDate(calculateEstimatedDeliveryDate(5))
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItemDTO cartItem : cartDTO.getCartItems()) {
            ProductDTO product = productServiceClient.getProductById(cartItem.getProductId());

            if (!productServiceClient.reserveProductStock(cartItem.getProductId(), cartItem.getQuantity())) {
                throw new RuntimeException("Failed to reserve stock for product: " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .imageUrl(product.getImageUrl())
                    .category(product.getCategory())
                    .build();

            order.addOrderItem(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        order.calculateFinalAmount();

        order = orderRepository.save(order);
        log.info("Order created successfully: {}", order.getOrderNumber());

        return mapToOrderDTO(order);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Order does not belong to user");
        }

        return mapToOrderDTO(order);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderByNumber(Long userId, String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Order does not belong to user");
        }

        return mapToOrderDTO(order);
    }

    @Transactional
    public OrderDTO cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Order does not belong to user");
        }

        if (!order.canBeCancelled()) {
            throw new RuntimeException("Order cannot be cancelled at this stage: " + order.getOrderStatus());
        }

        for (OrderItem item : order.getOrderItems()) {
            productServiceClient.releaseProductStock(item.getProductId(), item.getQuantity());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order = orderRepository.save(order);

        return mapToOrderDTO(order);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderByIdForAdmin(Long orderId) {
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        return mapToOrderDTO(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (dto.getOrderStatus() != null) order.setOrderStatus(dto.getOrderStatus());
        if (dto.getPaymentStatus() != null) order.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getTransactionId() != null) order.setTransactionId(dto.getTransactionId());
        if (dto.getEstimatedDeliveryDate() != null) order.setEstimatedDeliveryDate(dto.getEstimatedDeliveryDate());
        if (dto.getActualDeliveryDate() != null) order.setActualDeliveryDate(dto.getActualDeliveryDate());
        if (dto.getNotes() != null) order.setNotes(dto.getNotes());

        if (dto.getOrderStatus() == OrderStatus.DELIVERED && order.getActualDeliveryDate() == null) {
            order.setActualDeliveryDate(LocalDateTime.now());
        }

        order = orderRepository.save(order);
        return mapToOrderDTO(order);
    }

    private LocalDateTime calculateEstimatedDeliveryDate(int businessDays) {
        LocalDateTime date = LocalDateTime.now();
        int addedDays = 0;
        while (addedDays < businessDays) {
            date = date.plusDays(1);
            if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        return date;
    }

    private OrderDTO mapToOrderDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .imageUrl(item.getImageUrl())
                        .category(item.getCategory())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .notes(order.getNotes())
                .paymentMethod(order.getPaymentMethod())
                .taxAmount(order.getTaxAmount())
                .shippingAmount(order.getShippingAmount())
                .discountAmount(order.getDiscountAmount())
                .couponCode(order.getCouponCode())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                .actualDeliveryDate(order.getActualDeliveryDate())
                .transactionId(order.getTransactionId())
                .totalAmount(order.getTotalAmount())
                .finalAmount(order.getFinalAmount())
                .orderItems(items)
                .totalItems(order.getTotalItems())
                .build();
    }
}