package com.maximogiordano.users.controller;

import com.maximogiordano.users.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping("/users")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userDto;
    }
}
