package com.hiccup.Hiccupify.service.cart;

import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.model.CartItem;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.repository.CartItemRepository;
import com.hiccup.Hiccupify.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public Cart getCart(Long id) {
        //need proper logic why total amount and why set again and save
        return cartRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Cart not found"));
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart=cartRepository.findById(id).orElseThrow(()->new ResourceNotFound("Cart not found"));
       Set<CartItem> items= cart.getCartItems();
       items.clear();
       cart.setCartItems(items);
       cart.updateTotalAmount();
       cartRepository.save(cart);

//        Cart cart=getCart(id);
//        cartItemRepository.deleteAllByCartId(id);
//        cart.getCartItems().clear();
//
//        // my logic here we are getting optional of cart means T=cart, and trying to pass id but the parameter is asking for T os its throwing error
////        cartRepository.findById(id).ifPresentOrElse(id->cartRepository::deleteById,()->{throw new ResourceNotFound("cart not found")});\
//
//        System.out.println(id);
//        cartRepository.findById(id).ifPresentOrElse(cart -> cartRepository.deleteById(cart.getId()),()->{throw new ResourceNotFound("cart not found");});
//        System.out.println("cart deletion sucessfull");
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart=new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
