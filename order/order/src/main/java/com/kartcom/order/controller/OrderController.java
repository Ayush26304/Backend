package com.kartcom.order.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.kartcom.order.dto.OrderRequestDto;
import com.kartcom.order.dto.OrderResponseDto;
import com.kartcom.order.service.OrderService;
@RestController
@RequestMapping("/api/order")
public class OrderController {
 
    @Autowired private OrderService orderService;
 
    // User: Place an order
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> placeOrder(
        Authentication auth,
        @RequestHeader("Authorization") String token,
        @RequestBody OrderRequestDto request) {
 
        int userId = Integer.parseInt(auth.getName());
        OrderResponseDto response = orderService.placeOrder(userId, token, request);
        return ResponseEntity.ok(response);
    }
 
    // User: Get order by ID
    @GetMapping("/user/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
 
    // User: Get order history
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(Authentication auth) {
        int userId = Integer.parseInt(auth.getName());
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
 
    // User: Download invoice
    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
        byte[] data = orderService.downloadInvoice(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".txt")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data);
    }
 
    // User: Track order by invoice number
    @GetMapping("/track/{invoiceNumber}")
    public ResponseEntity<OrderResponseDto> trackOrder(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(orderService.getOrderByInvoice(invoiceNumber));
    }
 
    // Admin: Update order status
    @PutMapping("/updatestatus/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
        @PathVariable Long orderId,
        @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok().build();
    }
 
    // Admin: Get all orders
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
 
    // Admin: Delete an order
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }
}