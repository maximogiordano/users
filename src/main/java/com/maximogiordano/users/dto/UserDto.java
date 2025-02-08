package com.maximogiordano.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maximogiordano.users.validation.Password;
import com.maximogiordano.users.validation.UniquePhones;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Password
    private String password;

    @UniquePhones
    private List<@NotNull @Valid PhoneDto> phones;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime lastLogin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String token;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;
}
