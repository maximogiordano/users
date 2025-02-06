package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.entity.Phone;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneToPhoneDtoConverterTest {
    @Test
    void convert() {
        // given a PhoneToPhoneDtoConverter instance
        PhoneToPhoneDtoConverter phoneToPhoneDtoConverter = new PhoneToPhoneDtoConverter();

        // and a Phone instance
        Phone phone = new Phone();

        phone.setId(UUID.fromString("c179004d-5c62-4afb-894f-698921bf0249"));
        phone.setNumber(58949037L);
        phone.setCityCode(11);
        phone.setCountryCode("AR");

        // and the corresponding PhoneDto instance
        PhoneDto phoneDto = new PhoneDto();

        phoneDto.setId(UUID.fromString("c179004d-5c62-4afb-894f-698921bf0249"));
        phoneDto.setNumber(58949037L);
        phoneDto.setCityCode(11);
        phoneDto.setCountryCode("AR");

        // when the Phone instance is converted
        PhoneDto result = phoneToPhoneDtoConverter.convert(phone);

        // then the expected result is obtained
        assertEquals(phoneDto, result);
    }
}
