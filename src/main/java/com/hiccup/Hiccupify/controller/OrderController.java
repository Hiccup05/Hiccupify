package com.hiccup.Hiccupify.controller;


import com.hiccup.Hiccupify.dto.OrdersDto;
import com.hiccup.Hiccupify.model.Order;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("order/create_order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order=orderService.placeOrder(userId);
            OrdersDto ordersDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item ordered success!", ordersDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error in create order",e.getMessage()));
        }
    }

    @GetMapping("user/get/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrdersDto> userOrders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Item ordered succes!",userOrders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Opps!",e.getMessage()));
        }
    }



}
