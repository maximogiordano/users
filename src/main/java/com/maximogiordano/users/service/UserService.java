package com.maximogiordano.users.service;

import com.maximogiordano.users.dto.LoginDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.exception.ConflictException;
import com.maximogiordano.users.exception.CredentialsException;
import com.maximogiordano.users.exception.ResourceNotFoundException;
import com.maximogiordano.users.repository.UserRepository;
import com.maximogiordano.users.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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

    public UserDto signUp(@Valid UserDto userDto) {
        User user = Objects.requireNonNull(conversionService.convert(userDto, User.class));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("the given user already exists");
        }

        user = userRepository.save(user);

        return conversionService.convert(user, UserDto.class);
    }

    public UserDto login(LoginDto login) {
        Optional<User> userOptional = userRepository.findByEmail(login.getEmail());

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("the given user does not exist");
        }

        var user = userOptional.get();

        if (!user.getPassword().equals(login.getPassword())) {
            throw new CredentialsException("invalid credentials");
        }

        user.setLastLogin(dateTimeUtils.currentOffsetDateTime());
        user = userRepository.save(user);

        return conversionService.convert(user, UserDto.class);
    }
}
