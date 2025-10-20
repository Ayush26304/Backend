package com.ecommerce.CheckoutService.service;

import com.ecommerce.CheckoutService.DTO.*;
import com.ecommerce.CheckoutService.client.CartServiceClient;
import com.ecommerce.CheckoutService.client.OrderServiceClient;
import com.ecommerce.CheckoutService.client.ProductServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CheckoutService {

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private OrderServiceClient orderServiceClient;

    public OrderDTO processBuyNow(BuyNowRequestDTO request) {
        log.info("Processing Buy Now for userId: {}, productId: {}, quantity: {}",
                request.getUserId().intValue(), request.getProductId(), request.getQuantity());

        try {
            ProductDTO product = productServiceClient.getProductById(request.getProductId());
            log.debug("Fetched product details: {}", product);

            boolean reserved = productServiceClient.reserveProductStock(request.getProductId(), request.getQuantity());
            if (!reserved) {
                log.error("Stock reservation failed for product: {}", product.getName());
                throw new RuntimeException("Stock reservation failed for product: " + product.getName());
            }

            OrderItemDto orderItem = new OrderItemDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    request.getQuantity()
            );

            CreateOrderDTO orderRequest = new CreateOrderDTO();
            orderRequest.setUserId(request.getUserId());
            orderRequest.setShippingAddress(request.getShippingAddress());
            orderRequest.setBillingAddress(request.getShippingAddress());
            orderRequest.setPhoneNumber("N/A");
            orderRequest.setEmail("N/A");
            orderRequest.setNotes("Buy Now Order");
            orderRequest.setPaymentMethod(request.getPaymentMethod());
            orderRequest.setTaxAmount(BigDecimal.ZERO);
            orderRequest.setShippingAmount(BigDecimal.valueOf(50));
            orderRequest.setDiscountAmount(BigDecimal.ZERO);
            orderRequest.setCouponCode(null);
            orderRequest.setItems(List.of(orderItem));

            log.info("Sending order creation request to order-service...");
            OrderDTO response = orderServiceClient.createOrder(orderRequest);
            log.info("Order created successfully with orderNumber: {}", response.getOrderNumber());

            return response;

        } catch (Exception e) {
            log.error("Error during Buy Now checkout: {}", e.getMessage(), e);
            throw new RuntimeException("Checkout failed: " + e.getMessage());
        }
    }

    public CheckoutResponseDTO checkoutCart(CartCheckoutRequestDTO request) {
        log.info("Processing Cart Checkout for userId: {}", request.getUserId());

        try {
            CartDTO cart = cartServiceClient.getCartByUserId(request.getUserId());
            List<ProductSummaryDTO> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (CartItemDTO cartItem : cart.getCartItems()) {
                ProductDTO product = productServiceClient.getProductById(cartItem.getProductId());

                ProductSummaryDTO item = new ProductSummaryDTO(
                        product.getId().intValue(),
                        product.getName(),
                        product.getPrice(),
                        cartItem.getQuantity(),
                        product.getImageUrl()
                );

                items.add(item);
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }

            return new CheckoutResponseDTO(
                    items,
                    total,
                    List.of("COD", "UPI", "Credit Card", "Net Banking")
            );

        } catch (Exception e) {
            log.error("Error during Cart Checkout: {}", e.getMessage(), e);
            throw new RuntimeException("Cart checkout failed: " + e.getMessage());
        }
    }
}
