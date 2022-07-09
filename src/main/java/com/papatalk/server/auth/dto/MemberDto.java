package com.papatalk.server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    @NotNull
    private String phoneNumber;

    @NotNull
    private String password;

    private String email;
}
