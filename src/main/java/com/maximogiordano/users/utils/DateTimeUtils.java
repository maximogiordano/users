package com.maximogiordano.users.utils;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DateTimeUtils {
    public OffsetDateTime currentOffsetDateTime() {
        return OffsetDateTime.now();
    }
}
