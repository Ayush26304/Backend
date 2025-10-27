package com.kartcom.order.repositoy;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kartcom.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(int userId);
    Optional<Order> findByInvoiceNumber(String invoiceNumber);
}
