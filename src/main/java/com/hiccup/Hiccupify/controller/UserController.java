package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.dto.UserDto;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.request.CreateUserRequest;
import com.hiccup.Hiccupify.request.UserUpdateRequest;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.security.user.ShopUserDetails;
import com.hiccup.Hiccupify.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUserById(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ShopUserDetails principal = (ShopUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(new ApiResponse("User fetched successfully",principal));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/user/update/")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ShopUserDetails principal = (ShopUserDetails) authentication.getPrincipal();
            UserDto userDto=userService.convertToDto(userService.updateUser(userUpdateRequest,principal.getId()));
            return ResponseEntity.ok(new ApiResponse("Updated successfully",userDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Opps!:",e.getMessage()));
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(){
        try {
            ShopUserDetails principal =(ShopUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.deleteUser(principal.getId());
            return ResponseEntity.ok(new ApiResponse("User Deleted successfully",null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Opps!:",e.getMessage()));
        }

    }
}
