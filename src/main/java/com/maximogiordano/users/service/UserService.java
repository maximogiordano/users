package com.maximogiordano.users.service;

import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.repository.UserRepository;
import com.maximogiordano.users.utils.DateTimeUtils;
import com.maximogiordano.users.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final DateTimeUtils dateTimeUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserDto signUp(@Valid UserDto userDto) {
        User user = Objects.requireNonNull(conversionService.convert(userDto, User.class));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("the given user already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        userDto = Objects.requireNonNull(conversionService.convert(user, UserDto.class));
        userDto.setToken(jwtUtils.generateAccessToken(user.getEmail()));

        return userDto;
    }

    public UserDto login(String authHeader) {
        var token = authHeader.substring(7); // remove "Bearer " prefix
        String email = jwtUtils.extractUsername(token);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("the given user does not exist");
        }

        var user = userOptional.get();

        user.setLastLogin(dateTimeUtils.currentOffsetDateTime());
        user = userRepository.save(user);
        UserDto userDto = Objects.requireNonNull(conversionService.convert(user, UserDto.class));
        jwtUtils.invalidateToken(token);
        userDto.setToken(jwtUtils.generateAccessToken(email));

        return userDto;
    }
}
