package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.Phone;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoToUserConverter implements Converter<UserDto, User> {
    private final PhoneDtoToPhoneConverter phoneDtoToPhoneConverter;
    private final DateTimeUtils dateTimeUtils;

    @Override
    public User convert(UserDto source) {
        var target = new User();
        var now = dateTimeUtils.currentOffsetDateTime();

        target.setName(StringUtils.trimWhitespace(source.getName()));
        target.setPassword(StringUtils.trimWhitespace(source.getPassword()));
        target.setEmail(StringUtils.trimWhitespace(source.getEmail()));
        target.setPhones(toListOfPhone(source.getPhones()));
        target.setCreated(now);
        target.setLastLogin(now);
        target.setIsActive(true);

        return target;
    }

    private List<Phone> toListOfPhone(List<PhoneDto> phones) {
        if (phones == null) {
            return List.of();
        }

        return phones.stream()
                .map(phoneDtoToPhoneConverter::convert)
                .collect(Collectors.toList());
    }
}
