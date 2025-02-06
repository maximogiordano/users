package com.maximogiordano.users.controller;

import com.maximogiordano.users.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    @Test
    void test() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setPassword("J0hn.D03");
        UserController userController = new UserController();
        userDto = userController.createUser(userDto);
        assertEquals("John Doe", userDto.getName());
        assertEquals("J0hn.D03", userDto.getPassword());
    }
}
