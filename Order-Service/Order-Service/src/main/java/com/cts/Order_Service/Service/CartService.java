package com.cts.Order_Service.Service;

import com.cts.Order_Service.client.CartClient;
import com.cts.Order_Service.dto.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartClient cartClient;

    public CartDTO getCartByUserId(Long userId) {
        return cartClient.getCartByUserId(userId);
    }

    public boolean validateCartForCheckout(Long userId) {
        CartDTO cart = getCartByUserId(userId);
        return cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty();
    }

    public void clearCart(Long userId) {
        cartClient.clearCart(userId);
    }
}