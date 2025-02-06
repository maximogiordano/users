package com.maximogiordano.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ErrorDto {
    @JsonProperty("error")
    private List<ErrorItemDto> errors;
}
