package com.hiccup.Hiccupify.service.order;

import com.hiccup.Hiccupify.dto.OrdersDto;
import com.hiccup.Hiccupify.enums.OrderStatus;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.*;
import com.hiccup.Hiccupify.repository.OrderRepository;
import com.hiccup.Hiccupify.repository.ProductRepository;
import com.hiccup.Hiccupify.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart= cartService.getCartByUserId(userId);
        Order order=createOrder(cart);
        List<OrderItem> orderItems=createOrderItem(order,cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        cartService.clearCart(cart.getId());
        return orderRepository.save(order);
    }

    private Order createOrder(Cart cart){
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart){
       return cart.getCartItems().stream().map(cartItem->{
            Product product=cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(item->item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrdersDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFound("Order not found"));
    }

    @Override
    public List<OrdersDto> getUserOrders(Long userId){
       return orderRepository.findByUserId(userId)
               .orElseThrow(()->new ResourceNotFound("Order not found!"))
               .stream()
               .map(this::convertToDto)
               .toList();
    }

    @Override
    public OrdersDto convertToDto(Order order){
        return modelMapper.map(order, OrdersDto.class);
    }
}
