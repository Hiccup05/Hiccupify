package com.hiccup.Hiccupify.service.cart;

import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
}
