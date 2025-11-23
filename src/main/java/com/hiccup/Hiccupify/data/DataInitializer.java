package com.hiccup.Hiccupify.data;

import com.hiccup.Hiccupify.model.User;
import com.hiccup.Hiccupify.repository.UserRepository;
import com.hiccup.Hiccupify.request.CreateUserRequest;
import com.hiccup.Hiccupify.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("admin")==null) {
            userService.createUser(new CreateUserRequest(
                    "admin","123","admin","admin123"
            ));
        }
    }
}

