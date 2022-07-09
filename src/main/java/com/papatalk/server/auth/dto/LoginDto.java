package com.papatalk.server.auth.dto;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    private String phoneNumber;

   @NotNull
    private String password;
}