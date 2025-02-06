package com.maximogiordano.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PhoneDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    private Long number;

    @JsonProperty("citycode")
    @NotNull
    private Integer cityCode;

    @JsonProperty("countrycode")
    @NotBlank
    private String countryCode;
}
