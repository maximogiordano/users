package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.dto.UserDto;
import com.maximogiordano.users.entity.Phone;
import com.maximogiordano.users.entity.User;
import com.maximogiordano.users.utils.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDtoToUserConverterTest {
    @InjectMocks
    UserDtoToUserConverter userDtoToUserConverter;

    @Mock
    PhoneDtoToPhoneConverter phoneDtoToPhoneConverter;

    @Mock
    DateTimeUtils dateTimeUtils;

    @Test
    void convertWithListOfPhone() {
        // given a UserDto instance
        PhoneDto phoneDto = new PhoneDto();

        phoneDto.setNumber(58949037L);
        phoneDto.setCityCode(11);
        phoneDto.setCountryCode("AR");

        UserDto userDto = new UserDto();

        userDto.setName("JOHN DOE");
        userDto.setEmail("john.doe@email.com");
        userDto.setPassword("J0hn.D03");
        userDto.setPhones(List.of(phoneDto));

        // and the corresponding User instance
        Phone phone = new Phone();

        phone.setNumber(58949037L);
        phone.setCityCode(11);
        phone.setCountryCode("AR");

        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        User user = new User();

        user.setName("JOHN DOE");
        user.setEmail("john.doe@email.com");
        user.setPassword("J0hn.D03");
        user.setPhones(List.of(phone));
        user.setCreated(now);
        user.setLastLogin(now);
        user.setIsActive(true);

        // and the corresponding value returned by the currentOffsetDateTime method
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);

        // and the corresponding conversion for the PhoneDto instance
        when(phoneDtoToPhoneConverter.convert(phoneDto)).thenReturn(phone);

        // when the UserDto instance is converted
        User result = userDtoToUserConverter.convert(userDto);

        // then the currentOffsetDateTime method is called
        verify(dateTimeUtils).currentOffsetDateTime();

        // and the PhoneDto instance is converted
        verify(phoneDtoToPhoneConverter).convert(phoneDto);

        // and the expected result is obtained
        assertEquals(user, result);
    }

    @Test
    void convertWithoutListOfPhone() {
        // given a UserDto instance
        UserDto userDto = new UserDto();

        userDto.setName("JOHN DOE");
        userDto.setEmail("john.doe@email.com");
        userDto.setPassword("J0hn.D03");

        // and the corresponding User instance
        OffsetDateTime now = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);

        User user = new User();

        user.setName("JOHN DOE");
        user.setEmail("john.doe@email.com");
        user.setPassword("J0hn.D03");
        user.setPhones(List.of());
        user.setCreated(now);
        user.setLastLogin(now);
        user.setIsActive(true);

        // and the corresponding value returned by the currentOffsetDateTime method
        when(dateTimeUtils.currentOffsetDateTime()).thenReturn(now);

        // when the UserDto instance is converted
        User result = userDtoToUserConverter.convert(userDto);

        // then the currentOffsetDateTime method is called
        verify(dateTimeUtils).currentOffsetDateTime();

        // and the expected result is obtained
        assertEquals(user, result);
    }
}
