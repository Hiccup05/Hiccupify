package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.dto.UserDto;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.request.CreateUserRequest;
import com.hiccup.Hiccupify.request.UserUpdateRequest;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            UserDto userDto=userService.convertToDto(userService.getUserById(userId));
            return ResponseEntity.ok(new ApiResponse("User fetched successfully",userDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("user/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest userRequest){
        try {
            UserDto userDto=userService.convertToDto(userService.createUser(userRequest));
            return ResponseEntity.ok(new ApiResponse("User created succesfully",userDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("opps",e.getMessage()));
        }
    }

    @PutMapping("user/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,@PathVariable Long userId){
        try {
            UserDto userDto=userService.convertToDto(userService.updateUser(userUpdateRequest,userId));
            return ResponseEntity.ok(new ApiResponse("Updated sucessfully",userDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Opps!:",e.getMessage()));
        }

    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Deleted successfully",null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Opps!:",e.getMessage()));
        }

    }
}
