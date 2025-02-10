package com.maximogiordano.users.service;

import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.repository.UserRepository;
import com.maximogiordano.users.utils.DateTimeUtils;
import com.maximogiordano.users.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    PasswordEncoder passwordEncoder; // dependency

    @Mock
    JwtUtils jwtUtils; // dependency

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

        // and the same input with an encoded password
        User inputAsUserWithHashedPass = new User();

        inputAsUserWithHashedPass.setEmail("john.doe@email.com");
        inputAsUserWithHashedPass.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        inputAsUserWithHashedPass.setPhones(List.of());
        inputAsUserWithHashedPass.setCreated(now);
        inputAsUserWithHashedPass.setLastLogin(now);
        inputAsUserWithHashedPass.setIsActive(true);

        // and the created user
        User createdUser = new User();

        createdUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        createdUser.setEmail("john.doe@email.com");
        createdUser.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        createdUser.setPhones(List.of());
        createdUser.setCreated(now);
        createdUser.setLastLogin(now);
        createdUser.setIsActive(true);

        // and the created user as UserDto
        UserDto createdUserAsUserDto = new UserDto();

        createdUserAsUserDto.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        createdUserAsUserDto.setEmail("john.doe@email.com");
        createdUserAsUserDto.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        createdUserAsUserDto.setPhones(List.of());
        createdUserAsUserDto.setCreated(now);
        createdUserAsUserDto.setLastLogin(now);
        createdUserAsUserDto.setIsActive(true);

        // and the created user as UserDto with a generated token
        UserDto createdUserAsUserDtoWithToken = new UserDto();

        createdUserAsUserDtoWithToken.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        createdUserAsUserDtoWithToken.setEmail("john.doe@email.com");
        createdUserAsUserDtoWithToken.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        createdUserAsUserDtoWithToken.setPhones(List.of());
        createdUserAsUserDtoWithToken.setCreated(now);
        createdUserAsUserDtoWithToken.setLastLogin(now);
        createdUserAsUserDtoWithToken.setToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNv" +
                "bSIsImlhdCI6MTczOTE1ODY4MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLVC05T6f9bbISZxt4k6s_AKiA");
        createdUserAsUserDtoWithToken.setIsActive(true);

        // and the mocks behavior
        when(conversionService.convert(input, User.class)).thenReturn(inputAsUser);
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(false);
        when(passwordEncoder.encode("J0hn.D03")).thenReturn("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx" +
                "52Ti");
        when(userRepository.save(inputAsUserWithHashedPass)).thenReturn(createdUser);
        when(conversionService.convert(createdUser, UserDto.class)).thenReturn(createdUserAsUserDto);
        when(jwtUtils.generateAccessToken("john.doe@email.com")).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8" +
                "uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTczOTE1ODY4MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLV" +
                "C05T6f9bbISZxt4k6s_AKiA");

        // when the signUp method is called with the given input
        UserDto result = userService.signUp(input);

        // then the corresponding methods are called
        verify(conversionService).convert(input, User.class);
        verify(userRepository).existsByEmail("john.doe@email.com");
        verify(passwordEncoder).encode("J0hn.D03");
        verify(userRepository).save(inputAsUser);
        verify(conversionService).convert(createdUser, UserDto.class);
        verify(jwtUtils).generateAccessToken("john.doe@email.com");

        // and the expected result is obtained
        assertEquals(createdUserAsUserDtoWithToken, result);
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
        // given an authorization header used as input
        String input = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTczO" +
                "TE1ODY4MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLVC05T6f9bbISZxt4k6s_AKiA";

        // and the corresponding token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTczOTE1ODY4" +
                "MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLVC05T6f9bbISZxt4k6s_AKiA";

        // and a fixed current date and time with time offset
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 7, 0, 45, 0, 0, ZoneOffset.UTC);

        // and the generated token
        String generatedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTc" +
                "zOTE1ODY5MCwiZXhwIjoxNzM5MTU5NTkwfQ.-QtfdlgqQL1r3CQyqvcnsRFUaPQmWm0fu4LiOtvsmT4";

        // and the stored user for the given input
        User storedUser = new User();

        storedUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        storedUser.setEmail("john.doe@email.com");
        storedUser.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        storedUser.setPhones(List.of());
        storedUser.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        storedUser.setLastLogin(OffsetDateTime.of(2024, 2, 6, 23, 56, 13, 0, ZoneOffset.UTC));
        storedUser.setIsActive(true);

        // and the updated user
        User updatedUser = new User();

        updatedUser.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        updatedUser.setEmail("john.doe@email.com");
        updatedUser.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        updatedUser.setPhones(List.of());
        updatedUser.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        updatedUser.setLastLogin(now);
        updatedUser.setIsActive(true);

        // and the updated user as DTO
        UserDto updatedUserAsDto = new UserDto();

        updatedUserAsDto.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        updatedUserAsDto.setEmail("john.doe@email.com");
        updatedUserAsDto.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        updatedUserAsDto.setPhones(List.of());
        updatedUserAsDto.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        updatedUserAsDto.setLastLogin(now);
        updatedUserAsDto.setIsActive(true);

        // and the updated user as DTO with the generated token
        UserDto updatedUserAsDtoWithGeneratedToken = new UserDto();

        updatedUserAsDtoWithGeneratedToken.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        updatedUserAsDtoWithGeneratedToken.setEmail("john.doe@email.com");
        updatedUserAsDtoWithGeneratedToken.setPassword("$2a$12$PZZXY5FKak6RWpbjh59.Zu7.8Fd0QQsmWSuIL2/D5z3uAOXhx52Ti");
        updatedUserAsDtoWithGeneratedToken.setPhones(List.of());
        updatedUserAsDtoWithGeneratedToken.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        updatedUserAsDtoWithGeneratedToken.setLastLogin(now);
        updatedUserAsDtoWithGeneratedToken.setToken(generatedToken);
        updatedUserAsDtoWithGeneratedToken.setIsActive(true);

        // and the mocks behavior
        when(jwtUtils.extractUsername(token)).thenReturn("john.doe@email.com");
        when(userRepository.findByEmail("john.doe@email.com")).thenReturn(Optional.of(storedUser));
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(conversionService.convert(updatedUser, UserDto.class)).thenReturn(updatedUserAsDto);
        when(jwtUtils.generateAccessToken("john.doe@email.com")).thenReturn(generatedToken);

        // when the login method is called with the given input
        UserDto result = userService.login(input);

        // then the corresponding methods are called
        verify(jwtUtils).extractUsername(token);
        verify(userRepository).findByEmail("john.doe@email.com");
        verify(dateTimeUtils).currentOffsetDateTime();
        verify(userRepository).save(updatedUser);
        verify(conversionService).convert(updatedUser, UserDto.class);
        verify(jwtUtils).invalidateToken(token);
        verify(jwtUtils).generateAccessToken("john.doe@email.com");

        // and the expected result is obtained
        assertEquals(updatedUserAsDtoWithGeneratedToken, result);
    }

    @Test
    void testLoginWithNotFoundUser() {
        // given an authorization header used as input
        String input = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTczO" +
                "TE1ODY4MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLVC05T6f9bbISZxt4k6s_AKiA";

        // and the corresponding token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW8uZ2lvcmRhbm8xMEBob3RtYWlsLmNvbSIsImlhdCI6MTczOTE1ODY4" +
                "MSwiZXhwIjoxNzM5MTU5NTgxfQ.Z6aQ1AhgEat-g_5PPwLVC05T6f9bbISZxt4k6s_AKiA";

        // and the mocks behavior
        when(jwtUtils.extractUsername(token)).thenReturn("john.doe@email.com");
        when(userRepository.findByEmail("john.doe@email.com")).thenReturn(Optional.empty());

        // when the login method is called with the given input
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> userService.login(input));

        // then the corresponding methods are called
        verify(jwtUtils).extractUsername(token);
        verify(userRepository).findByEmail("john.doe@email.com");

        // and the expected exception is obtained
        assertEquals("the given user does not exist", e.getMessage());
    }
}
