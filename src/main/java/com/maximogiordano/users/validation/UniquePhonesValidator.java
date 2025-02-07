package com.maximogiordano.users.validation;

import com.maximogiordano.users.dto.PhoneDto;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class UniquePhonesValidator implements ConstraintValidator<UniquePhones, List<PhoneDto>> {
    @Override
    public boolean isValid(List<PhoneDto> value, ConstraintValidatorContext context) {
        return !hasDuplicates(value);
    }

    private boolean hasDuplicates(List<PhoneDto> phones) {
        if (phones == null) {
            return false;
        }

        return phones.stream()
                .filter(Objects::nonNull) // ignore null values
                .collect(groupingBy(this::key, counting()))
                .values()
                .stream()
                .anyMatch(count -> count > 1);
    }

    private List<Object> key(PhoneDto phone) {
        return Arrays.asList(phone.getNumber(), phone.getCityCode(),
                StringUtils.trimWhitespace(phone.getCountryCode()));
    }
}
