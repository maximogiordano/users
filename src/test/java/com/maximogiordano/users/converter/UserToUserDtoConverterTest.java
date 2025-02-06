package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.Phone;
import com.maximogiordano.users.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserToUserDtoConverterTest {
    @InjectMocks
    UserToUserDtoConverter userToUserDtoConverter; // system under test

    @Mock
    PhoneToPhoneDtoConverter phoneToPhoneDtoConverter; // dependency

    @Test
    void convert() {
        // given a User instance
        Phone phone = new Phone();

        phone.setId(UUID.fromString("c179004d-5c62-4afb-894f-698921bf0249"));
        phone.setNumber(58949037L);
        phone.setCityCode(11);
        phone.setCountryCode("AR");

        User user = new User();

        user.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        user.setName("JOHN DOE");
        user.setEmail("john.doe@email.com");
        user.setPassword("J0hn.D03");
        user.setPhones(List.of(phone));
        user.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        user.setLastLogin(OffsetDateTime.of(2024, 2, 6, 23, 1, 0, 0, ZoneOffset.UTC));
        user.setIsActive(true);

        // and the corresponding UserDto instance
        PhoneDto phoneDto = new PhoneDto();

        phoneDto.setId(UUID.fromString("c179004d-5c62-4afb-894f-698921bf0249"));
        phoneDto.setNumber(58949037L);
        phoneDto.setCityCode(11);
        phoneDto.setCountryCode("AR");

        UserDto userDto = new UserDto();

        userDto.setId(UUID.fromString("2871e0f1-f35a-4997-b27f-a961d2cc05ad"));
        userDto.setName("JOHN DOE");
        userDto.setEmail("john.doe@email.com");
        userDto.setPassword("J0hn.D03");
        userDto.setPhones(List.of(phoneDto));
        userDto.setCreated(OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC));
        userDto.setLastLogin(OffsetDateTime.of(2024, 2, 6, 23, 1, 0, 0, ZoneOffset.UTC));
        userDto.setIsActive(true);

        // and the corresponding conversion for the Phone instance
        when(phoneToPhoneDtoConverter.convert(phone)).thenReturn(phoneDto);

        // when the User instance is converted
        UserDto result = userToUserDtoConverter.convert(user);

        // then the Phone instance is converted
        verify(phoneToPhoneDtoConverter).convert(phone);

        // and the expected result is obtained
        assertEquals(userDto, result);
    }
}
