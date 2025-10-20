package com.cts.Order_Service.Controller;

import com.cts.Order_Service.Service.OrderService;
import com.cts.Order_Service.Service.CartService;
import com.cts.Order_Service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    /**
     * Create order from user's cart
     */
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam Long userId,
                                                @RequestBody CreateOrderDTO createOrderDTO) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        OrderDTO orderDTO = orderService.createOrderFromCart(userId, createOrderDTO, cartDTO);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Get order by ID for user
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@RequestParam Long userId,
                                                 @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(userId, orderId));
    }

    /**
     * Get order by order number for user
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrderByNumber(@RequestParam Long userId,
                                                     @PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByNumber(userId, orderNumber));
    }

    /**
     * Cancel order
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@RequestParam Long userId,
                                                @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }

    /**
     * Get order by ID for admin
     */
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByIdForAdmin(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderByIdForAdmin(orderId));
    }

    /**
     * Update order status (admin only)
     */
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestBody UpdateOrderStatusDTO updateDTO) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, updateDTO));
    }
}
