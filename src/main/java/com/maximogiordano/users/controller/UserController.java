package com.maximogiordano.users.controller;

import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public UserDto signUp(@RequestBody UserDto userDto) {
        return userService.signUp(userDto);
    }

    @PostMapping("/login")
    public UserDto login(@RequestHeader("Authorization") String authHeader) {
        return userService.login(authHeader);
    }
}
