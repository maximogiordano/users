package com.maximogiordano.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ErrorItemDto {
    private OffsetDateTime timestamp;

    @JsonProperty("codigo")
    private Integer code;

    private String detail;
}
