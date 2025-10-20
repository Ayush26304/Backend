package com.cts.Order_Service.Entity;



import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String shippingAddress;
    private String billingAddress;
    private String phoneNumber;
    private String email;
    private String notes;
    private String paymentMethod;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private String couponCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String transactionId;

    private BigDecimal totalAmount;
    private BigDecimal finalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void calculateFinalAmount() {
        BigDecimal total = totalAmount.add(taxAmount).add(shippingAmount);
        if (discountAmount != null) {
            total = total.subtract(discountAmount);
        }
        this.finalAmount = total;
    }

    public int getTotalItems() {
        return orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public boolean canBeCancelled() {
        return orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.CONFIRMED;
    }
}

