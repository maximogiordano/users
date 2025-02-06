package com.maximogiordano.users.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
