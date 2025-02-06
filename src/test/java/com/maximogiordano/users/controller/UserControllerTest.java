package com.maximogiordano.users.controller;

import com.maximogiordano.users.dto.LoginDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController; // system under test

    @Mock
    UserService userService; // dependency

    @Test
    void signUp() {
        // given the instance used as input by the controller
        UserDto input = new UserDto();
        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D03");

        // and the instance used as output by the service
        UserDto output = new UserDto();
        output.setEmail(input.getEmail());
        output.setPassword(input.getPassword());
        output.setId(UUID.randomUUID());
        when(userService.signUp(input)).thenReturn(output);

        // when the controller signUp method is called
        UserDto result = userController.signUp(input);

        // then the service signUp method is called with the same input
        verify(userService).signUp(input);

        // and the controller returns the same instance as the service
        assertEquals(output, result);
    }

    @Test
    void login() {
        // given the instance used as input by the controller
        LoginDto input = new LoginDto();
        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D03");

        // and the instance used as output by the service
        UserDto output = new UserDto();
        output.setEmail(input.getEmail());
        output.setPassword(input.getPassword());
        output.setId(UUID.randomUUID());
        when(userService.login(input)).thenReturn(output);

        // when the controller login method is called
        UserDto result = userController.login(input);

        // then the service login method is called with the same input
        verify(userService).login(input);

        // and the controller returns the same instance as the service
        assertEquals(output, result);
    }
}
