package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.model.CartItem;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.cart.ICartItemService;
import com.hiccup.Hiccupify.service.cart.ICartService;
import com.hiccup.Hiccupify.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    public final ICartItemService cartItemService;
    public final ICartService cartService;
    private final IUserService userService;

    @GetMapping("cart_item/get")
    public ResponseEntity<ApiResponse> getCartItem(@RequestParam Long cartId,@RequestParam Long itemId){
        try {
            CartItem cartItem=cartItemService.getCartItem(cartId, itemId);
            return  ResponseEntity.ok(new ApiResponse("Item fetched successfully",cartItem));
        } catch (RuntimeException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("cart_item/add")
    public ResponseEntity<ApiResponse> addCartItem(@RequestParam Long itemId,@RequestParam int quantity){
        try {
            User user=userService.getAuthenticatedUser();
            Cart cart=cartService.initializeNewCart(user);
            cartItemService.addCartItem(cart.getId(),itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item is added successfully",null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        catch(JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("cart_item/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId,@RequestParam Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Cart item is removed from cart",null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus. NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("cart_item/quantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam Long cartId, @RequestParam Long itemId, @RequestParam int quantity){
        try {
            cartItemService.updateItemQuantity(cartId, itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Quantity is updated successfully",null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
