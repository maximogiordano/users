package com.maximogiordano.users.validation;

import com.maximogiordano.users.dto.PhoneDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniquePhonesValidatorTest {
    UniquePhonesValidator uniquePhonesValidator = new UniquePhonesValidator(); // system under test

    @ParameterizedTest
    @MethodSource("testData")
    void test(List<PhoneDto> value, boolean expected) {
        assertEquals(expected, uniquePhonesValidator.isValid(value, null));
    }

    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(List.of(createPhone(null, null, null), createPhone(null, null, null)), false),
                Arguments.of(List.of(createPhone(58949037L, null, null), createPhone(58949037L, null, null)), false),
                Arguments.of(List.of(createPhone(null, 11, null), createPhone(null, 11, null)), false),
                Arguments.of(List.of(createPhone(null, null, "AR"), createPhone(null, null, "AR")), false),
                Arguments.of(List.of(createPhone(58949037L, 11, null), createPhone(58949037L, 11, null)), false),
                Arguments.of(List.of(createPhone(58949037L, null, "AR"), createPhone(58949037L, null, "AR")), false),
                Arguments.of(List.of(createPhone(null, 11, "AR"), createPhone(null, 11, "AR")), false),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 11, "AR")), false),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 11, " AR")), false),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 11, "AR ")), false),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 11, " AR ")), false),
                Arguments.of(List.of(createPhone(58949032L, 11, "AR"), createPhone(58949037L, 11, "AR")), true),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 2227, "AR")), true),
                Arguments.of(List.of(createPhone(58949037L, 11, "AR"), createPhone(58949037L, 11, "BR")), true),
                Arguments.of(Arrays.asList(createPhone(58949032L, 11, "AR"), null, createPhone(58949037L, 11, "AR")), true),
                Arguments.of(Arrays.asList(createPhone(58949037L, 11, "AR"), null, createPhone(58949037L, 11, "AR")), false)
        );
    }

    static PhoneDto createPhone(Long number, Integer cityCode, String countryCode) {
        PhoneDto phone = new PhoneDto();

        phone.setNumber(number);
        phone.setCityCode(cityCode);
        phone.setCountryCode(countryCode);

        return phone;
    }
}
