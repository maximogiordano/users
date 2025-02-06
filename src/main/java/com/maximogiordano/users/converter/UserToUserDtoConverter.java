package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.Phone;
import com.maximogiordano.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    private final PhoneToPhoneDtoConverter phoneToPhoneDtoConverter;

    @Override
    public UserDto convert(User source) {
        var target = new UserDto();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setPassword(source.getPassword());
        target.setEmail(source.getEmail());
        target.setPhones(toListOfPhoneDto(source.getPhones()));
        target.setCreated(source.getCreated());
        target.setLastLogin(source.getLastLogin());
        target.setIsActive(source.getIsActive());

        return target;
    }

    private List<PhoneDto> toListOfPhoneDto(List<Phone> phones) {
        return phones.stream()
                .map(phoneToPhoneDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
