package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.entity.Phone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneDtoToPhoneConverterTest {
    @Test
    void convert() {
        // given a PhoneDtoToPhoneConverter instance
        PhoneDtoToPhoneConverter phoneDtoToPhoneConverter = new PhoneDtoToPhoneConverter();

        // and a PhoneDto instance
        PhoneDto phoneDto = new PhoneDto();

        phoneDto.setNumber(58949037L);
        phoneDto.setCityCode(11);
        phoneDto.setCountryCode("AR");

        // and the corresponding Phone instance
        Phone phone = new Phone();

        phone.setNumber(58949037L);
        phone.setCityCode(11);
        phone.setCountryCode("AR");

        // when the PhoneDto instance is converted
        Phone result = phoneDtoToPhoneConverter.convert(phoneDto);

        // then the expected result is obtained
        assertEquals(phone, result);
    }
}
