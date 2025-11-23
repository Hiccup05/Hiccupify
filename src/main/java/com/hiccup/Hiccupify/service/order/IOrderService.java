package com.hiccup.Hiccupify.service.order;

import com.hiccup.Hiccupify.dto.OrdersDto;
import com.hiccup.Hiccupify.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrdersDto getOrder(Long orderId);

    List<OrdersDto> getUserOrders(Long userId);

    OrdersDto convertToDto(Order order);
}
