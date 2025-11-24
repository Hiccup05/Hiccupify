package com.hiccup.Hiccupify.controller;


import com.hiccup.Hiccupify.dto.OrdersDto;
import com.hiccup.Hiccupify.model.Order;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.security.user.ShopUserDetails;
import com.hiccup.Hiccupify.service.order.IOrderService;
import com.hiccup.Hiccupify.service.user.IUserService;
import com.hiccup.Hiccupify.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;

    @PostMapping("order/create_order")
    public ResponseEntity<ApiResponse> createOrder(){
        try {
            ShopUserDetails principal = (ShopUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Order order=orderService.placeOrder(principal.getId());
            OrdersDto ordersDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item ordered success!", ordersDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error in create order",e.getMessage()));
        }
    }

    @GetMapping("user/get")
    public ResponseEntity<ApiResponse> getUserOrders(){
        try {
            User authenticatedUser = userService.getAuthenticatedUser();
            List<OrdersDto> userOrders = orderService.getUserOrders(authenticatedUser.getId());
            return ResponseEntity.ok(new ApiResponse("Item ordered success!",userOrders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Opps!",e.getMessage()));
        }
    }



}
