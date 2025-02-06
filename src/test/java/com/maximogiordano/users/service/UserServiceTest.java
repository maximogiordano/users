package com.maximogiordano.users.service;

import com.maximogiordano.users.dto.LoginDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.CredentialsException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.repository.UserRepository;
import com.maximogiordano.users.utils.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService; // system under test

    @Mock
    UserRepository userRepository; // dependency

    @Mock
    ConversionService conversionService; // dependency

    @Mock
    DateTimeUtils dateTimeUtils; // dependency

    @Test
    void signUpOK() {
        // given an input
        UserDto input = new UserDto();

        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D03");

        // and a fixed current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        // and the input as User
        User inputAsUser = new User();

        inputAsUser.setEmail("john.doe@email.com");
        inputAsUser.setPassword("J0hn.D03");
        inputAsUser.setPhones(List.of());
        inputAsUser.setCreated(now);
        inputAsUser.setLastLogin(now);
        inputAsUser.setIsActive(true);

        // and the created user
        User createdUser = new User();

        createdUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        createdUser.setEmail("john.doe@email.com");
        createdUser.setPassword("J0hn.D03");
        createdUser.setPhones(List.of());
        createdUser.setCreated(now);
        createdUser.setLastLogin(now);
        createdUser.setIsActive(true);

        // and the created user as UserDto
        UserDto createdUserAsUserDto = new UserDto();

        createdUserAsUserDto.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        createdUserAsUserDto.setEmail("john.doe@email.com");
        createdUserAsUserDto.setPassword("J0hn.D03");
        createdUserAsUserDto.setPhones(List.of());
        createdUserAsUserDto.setCreated(now);
        createdUserAsUserDto.setLastLogin(now);
        createdUserAsUserDto.setIsActive(true);

        // and the mocks behavior
        when(conversionService.convert(input, User.class)).thenReturn(inputAsUser);
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(false);
        when(userRepository.save(inputAsUser)).thenReturn(createdUser);
        when(conversionService.convert(createdUser, UserDto.class)).thenReturn(createdUserAsUserDto);

        // when the signUp method is called with the given input
        UserDto result = userService.signUp(input);

        // then the corresponding methods are called
        verify(conversionService).convert(input, User.class);
        verify(userRepository).existsByEmail("john.doe@email.com");
        verify(userRepository).save(inputAsUser);
        verify(conversionService).convert(createdUser, UserDto.class);

        // and the expected result is obtained
        assertEquals(createdUserAsUserDto, result);
    }

    @Test
    void signUpNoOk() {
        // given an input
        UserDto input = new UserDto();

        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D03");

        // and a fixed current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        // and the input as User
        User inputAsUser = new User();

        inputAsUser.setEmail("john.doe@email.com");
        inputAsUser.setPassword("J0hn.D03");
        inputAsUser.setPhones(List.of());
        inputAsUser.setCreated(now);
        inputAsUser.setLastLogin(now);
        inputAsUser.setIsActive(true);

        // and the mocks behavior
        when(conversionService.convert(input, User.class)).thenReturn(inputAsUser);
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(true);

        // when the signUp method is called with the given input
        ConflictException e = assertThrows(ConflictException.class, () -> userService.signUp(input));

        // then the corresponding methods are called
        verify(conversionService).convert(input, User.class);
        verify(userRepository).existsByEmail("john.doe@email.com");

        // and the expected exception is obtained
        assertEquals("the given user already exists", e.getMessage());
    }

    @Test
    void testLoginOk() {
        // given an input
        LoginDto input = new LoginDto();

        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D03");

        // and a fixed current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 7, 0, 45, 0, 0, ZoneOffset.UTC);

        // and the stored user for the given input
        User storedUser = new User();

        storedUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        storedUser.setEmail("john.doe@email.com");
        storedUser.setPassword("J0hn.D03");
        storedUser.setPhones(List.of());
        storedUser.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        storedUser.setLastLogin(OffsetDateTime.of(2024, 2, 6, 23, 56, 13, 0, ZoneOffset.UTC));
        storedUser.setIsActive(true);

        // and the updated user
        User updatedUser = new User();

        updatedUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        updatedUser.setEmail("john.doe@email.com");
        updatedUser.setPassword("J0hn.D03");
        updatedUser.setPhones(List.of());
        updatedUser.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        updatedUser.setLastLogin(now);
        updatedUser.setIsActive(true);

        // and the updated user as DTO
        UserDto updatedUserAsDto = new UserDto();

        updatedUserAsDto.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        updatedUserAsDto.setEmail("john.doe@email.com");
        updatedUserAsDto.setPassword("J0hn.D03");
        updatedUserAsDto.setPhones(List.of());
        updatedUserAsDto.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        updatedUserAsDto.setLastLogin(now);
        updatedUserAsDto.setIsActive(true);

        // and the mocks behavior
        when(userRepository.findByEmail("john.doe@email.com")).thenReturn(Optional.of(storedUser));
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(conversionService.convert(updatedUser, UserDto.class)).thenReturn(updatedUserAsDto);

        // when the login method is called with the given input
        UserDto result = userService.login(input);

        // then the corresponding methods are called
        verify(userRepository).findByEmail("john.doe@email.com");
        verify(dateTimeUtils).currentOffsetDateTime();
        verify(userRepository).save(updatedUser);
        verify(conversionService).convert(updatedUser, UserDto.class);

        // and the expected exception is obtained
        assertEquals(updatedUserAsDto, result);
    }

    @Test
    void testLoginWithInvalidPassword() {
        // given an input
        LoginDto input = new LoginDto();

        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D0e");

        // and the stored user for the given input
        User storedUser = new User();

        storedUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        storedUser.setEmail("john.doe@email.com");
        storedUser.setPassword("J0hn.D03");
        storedUser.setPhones(List.of());
        storedUser.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        storedUser.setLastLogin(OffsetDateTime.of(2024, 2, 6, 23, 56, 13, 0, ZoneOffset.UTC));
        storedUser.setIsActive(true);

        // and the mocks behavior
        when(userRepository.findByEmail("john.doe@email.com")).thenReturn(Optional.of(storedUser));

        // when the login method is called with the given input
        CredentialsException e = assertThrows(CredentialsException.class, () -> userService.login(input));

        // then the corresponding methods are called
        verify(userRepository).findByEmail("john.doe@email.com");

        // and the expected exception is obtained
        assertEquals("invalid credentials", e.getMessage());
    }

    @Test
    void testLoginWithInvalidEmail() {
        // given an input
        LoginDto input = new LoginDto();

        input.setEmail("john.doe@email.com");
        input.setPassword("J0hn.D0e");

        // and the mocks behavior
        when(userRepository.findByEmail("john.doe@email.com")).thenReturn(Optional.empty());

        // when the login method is called with the given input
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> userService.login(input));

        // then the corresponding methods are called
        verify(userRepository).findByEmail("john.doe@email.com");

        // and the expected exception is obtained
        assertEquals("the given user does not exist", e.getMessage());
    }
}
