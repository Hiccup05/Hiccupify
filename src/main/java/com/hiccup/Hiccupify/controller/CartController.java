package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    public final CartService cartService;


    @GetMapping("/cart/{id}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id){
        try{
            Cart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("cart found",cart));
        }
        catch (RuntimeException e){
            System.out.println("came here");
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage()+"came here",null));
        }
    }

    @DeleteMapping("cart/delete/{id}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id){
        try{
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("cart has been deleted successfully",null));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("cart/total_price/{id}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id){
        try{
            BigDecimal totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("total price ",totalPrice));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable Long id){
        try{
            Cart cart = cartService.getCartByUserId(id);
            return ResponseEntity.ok(new ApiResponse("cart found",cart));
        }
        catch (RuntimeException e){
            System.out.println("came here");
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage()+"came here",null));
        }
    }
}
