package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.entity.Phone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhoneToPhoneDtoConverter implements Converter<Phone, PhoneDto> {
    @Override
    public PhoneDto convert(Phone source) {
        var target = new PhoneDto();

        target.setId(source.getId());
        target.setNumber(source.getNumber());
        target.setCityCode(source.getCityCode());
        target.setCountryCode(source.getCountryCode());

        return target;
    }
}
