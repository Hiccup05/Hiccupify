package com.hiccup.Hiccupify.service.user;

import com.hiccup.Hiccupify.dto.UserDto;
import com.hiccup.Hiccupify.exception.AlreadyExistException;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.repository.UserRepository;
import com.hiccup.Hiccupify.request.CreateUserRequest;
import com.hiccup.Hiccupify.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFound("User Not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {

        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User createdUser=new User();
                    createdUser.setFirstName(req.getFirstName());
                    createdUser.setLastName(req.getLastName());
                    createdUser.setEmail(req.getEmail());
                    createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
                    return userRepository.save(createdUser);
                }).orElseThrow(()->new AlreadyExistException(request.getEmail()+"  already exists"));
    }


    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser->{
                    existingUser.setLastName(request.getLastName());
                    existingUser.setFirstName(request.getFirstName());
                    return userRepository.save(existingUser);
                }).orElseThrow(()->new ResourceNotFound("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,()->{throw new ResourceNotFound("User is already deleted or never existed");});
    }

    @Override
    public UserDto convertToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email);
    }
}
