package com.maximogiordano.users.utils;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilsTest {
    @Test
    void testCurrentOffsetDateTime() {
        try (MockedStatic<OffsetDateTime> mockedStatic = Mockito.mockStatic(OffsetDateTime.class)) {
            // given a DateTimeUtils instance
            DateTimeUtils dateTimeUtils = new DateTimeUtils();

            // and a fixed OffsetDateTime instance
            OffsetDateTime fixedOffsetDateTime = OffsetDateTime.of(2024, 2, 6, 10, 15, 30, 0, ZoneOffset.UTC);
            mockedStatic.when(OffsetDateTime::now).thenReturn(fixedOffsetDateTime);

            // when the currentOffsetDateTime method is called
            OffsetDateTime result = dateTimeUtils.currentOffsetDateTime();

            // then the expected result is obtained
            assertEquals(fixedOffsetDateTime, result);
        }
    }
}
