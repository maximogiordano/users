package com.maximogiordano.users.converter;

import com.maximogiordano.users.dto.PhoneDto;
import com.maximogiordano.users.entity.Phone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PhoneDtoToPhoneConverter implements Converter<PhoneDto, Phone> {
    @Override
    public Phone convert(PhoneDto source) {
        var target = new Phone();

        target.setNumber(source.getNumber());
        target.setCityCode(source.getCityCode());
        target.setCountryCode(StringUtils.trimWhitespace(source.getCountryCode()));

        return target;
    }
}
