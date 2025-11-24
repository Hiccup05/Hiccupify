package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.cart.CartService;
import com.hiccup.Hiccupify.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    public final CartService cartService;
    private final IUserService userService;

    @GetMapping("/cart/{id}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id){
        try{
            Cart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("cart found",cart));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage()+"came here",null));
        }
    }

    @DeleteMapping("/cart/delete")
    public ResponseEntity<ApiResponse> clearCart(){
        try{
            Cart cart = cartService.getCartByUserId(userService.getAuthenticatedUser().getId());
            cartService.clearCart(cart.getId());
            return ResponseEntity.ok(new ApiResponse("cart has been deleted successfully",null));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/cart/total_price")
    public ResponseEntity<ApiResponse> getTotalAmount(){
        try{
            Cart cart = cartService.getCartByUserId(userService.getAuthenticatedUser().getId());
            BigDecimal totalPrice = cartService.getTotalPrice(cart.getId());
            return ResponseEntity.ok(new ApiResponse("total price ",totalPrice));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getCartByUserId(){
        try{
            Cart cart = cartService.getCartByUserId(userService.getAuthenticatedUser().getId());
            return ResponseEntity.ok(new ApiResponse("cart found",cart));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage()+"came here",null));
        }
    }
}
