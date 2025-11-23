package com.hiccup.Hiccupify.service.user;

import com.hiccup.Hiccupify.dto.UserDto;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.request.CreateUserRequest;
import com.hiccup.Hiccupify.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userID);

    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
