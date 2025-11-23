package com.hiccup.Hiccupify.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrdersDto> order;
    private CartDto cart;
}
