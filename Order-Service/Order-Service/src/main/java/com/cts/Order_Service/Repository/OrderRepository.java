package com.cts.Order_Service.Repository;

import com.cts.Order_Service.Entity.Order;
import com.cts.Order_Service.Entity.OrderStatus;
import com.cts.Order_Service.Entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findByIdWithOrderItems(Long id);

    Optional<Order> findByOrderNumber(String orderNumber);

    long countByOrderStatus(OrderStatus status);

    long countByPaymentStatus(PaymentStatus status);

    @Query("SELECT o FROM Order o WHERE " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:orderStatus IS NULL OR o.orderStatus = :orderStatus) AND " +
           "(:paymentStatus IS NULL OR o.paymentStatus = :paymentStatus) AND " +
           "(:startDate IS NULL OR o.estimatedDeliveryDate >= :startDate) AND " +
           "(:endDate IS NULL OR o.estimatedDeliveryDate <= :endDate) AND " +
           "(LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(o.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Order> findOrdersWithFiltersAndSearch(Long userId,
                                               OrderStatus orderStatus,
                                               PaymentStatus paymentStatus,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate,
                                               String search,
                                               Pageable pageable);
}